package at.backend.MarketingCompany.config.ratelimit;

import at.backend.MarketingCompany.config.ratelimit.base.GraphQLRateLimit;
import at.backend.MarketingCompany.config.ratelimit.base.RateLimitType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.Order;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Component
@Order(2)
@Slf4j
@RequiredArgsConstructor
public class GraphQLOperationRateLimitInterceptor implements WebGraphQlInterceptor {

	private final RedisRateLimiter rateLimiter;
	private final RateLimitProperties properties;
	private final GraphQLRateLimitRegistry rateLimitRegistry;

	@Override
	public @NotNull Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
		String operationName = extractOperationName(request.getDocument());

		log.info("=== OPERATION RATE LIMIT CHECK: {} ===", operationName);

		// Search if there's a rate limit for this operation
		GraphQLRateLimit rateLimit = rateLimitRegistry.getRateLimitForOperation(operationName);

		if (rateLimit == null) {
			log.info("No rate limit configured for operation: {}", operationName);
			return chain.next(request);
		}

		log.info("Rate limit found for {}: profile={}, type={}",
				operationName, rateLimit.value(), rateLimit.type());

		String profile = rateLimit.value();

		// Get configuration
		int maxRequests;
		int durationSeconds;

		if (properties.getEndpoints().containsKey(profile)) {
			RateLimitProperties.EndpointConfig config = properties.getEndpoints().get(profile);
			maxRequests = config.getMaxRequests();
			durationSeconds = config.getDurationSeconds();
			log.info("Using profile '{}': {} req/{}s", profile, maxRequests, durationSeconds);
		} else {
			maxRequests = properties.getDefaults().getMaxRequests();
			durationSeconds = properties.getDefaults().getDurationSeconds();
			log.warn("Profile '{}' not found, using defaults: {} req/{}s",
					profile, maxRequests, durationSeconds);
		}

		// Build key for rate limiting
		String key = buildKey(request, operationName, rateLimit.type());
		log.info("Rate limit key: {}", key);

		// Verify rate limit before processing
		boolean isAllowed = rateLimiter.isAllowed(key, maxRequests, Duration.ofSeconds(durationSeconds));

		if (!isAllowed) {
			RedisRateLimiter.RateLimitInfo info = rateLimiter.getRateLimitInfo(key, maxRequests);
			log.warn("RATE LIMIT EXCEEDED for {} - Remaining: {}, Reset in: {}s",
					operationName, info.getRemaining(), info.getResetAfter());

			return Mono.error(new RuntimeException(
					String.format("Rate limit exceeded for operation '%s'. Try again in %d seconds.",
							operationName, info.getResetAfter())
			));
		}

		// Pass
		return chain.next(request).map(response -> {
			// Get Info after processing
			RedisRateLimiter.RateLimitInfo updatedInfo =
					rateLimiter.getRateLimitInfo(key, maxRequests);

			log.info("Rate limit OK for {} - Remaining: {}, Reset in: {}s",
					operationName, updatedInfo.getRemaining(), updatedInfo.getResetAfter());

			HttpHeaders headers = response.getResponseHeaders();
			headers.add("X-RateLimit-Operation-Limit", String.valueOf(maxRequests));
			headers.add("X-RateLimit-Operation-Remaining", String.valueOf(updatedInfo.getRemaining()));
			headers.add("X-RateLimit-Operation-Reset", String.valueOf(updatedInfo.getResetAfter()));
			headers.add("X-RateLimit-Operation-Window", durationSeconds + " seconds");
			headers.add("X-RateLimit-Operation-Name", operationName);
			headers.add("X-RateLimit-Operation-Profile", profile);

			return response;
		});
	}

	private String buildKey(WebGraphQlRequest request, String operationName, RateLimitType type) {
		return switch (type) {
			case IP_BASED -> {
				String ip = getClientIp(request);
				yield String.format("graphql:%s:ip:%s", operationName, ip);
			}
			case USER_BASED -> {
				String userId = getUserId(request);
				yield String.format("graphql:%s:user:%s", operationName, userId);
			}
			case GLOBAL -> String.format("graphql:%s", operationName);
			default -> String.format("graphql:%s:custom", operationName);
		};
	}

	private String getClientIp(WebGraphQlRequest request) {
		List<String> xfHeader = request.getHeaders().get("X-Forwarded-For");
		if (xfHeader != null && !xfHeader.isEmpty()) {
			return xfHeader.get(0).split(",")[0].trim();
		}
		return request.getRemoteAddress() != null ?
				request.getRemoteAddress().getAddress().getHostAddress() : "unknown";
	}

	private String getUserId(WebGraphQlRequest request) {
		// TODO: Get From Auth Context
		return "anonymous";
	}

	private String extractOperationName(String document) {
		if (document == null || document.isBlank()) {
			return "unknown";
		}

		// Search for the name of the operation
		// Format: "mutation SignUp(...)" o "query GetUser(...)"
		String[] lines = document.split("\n");
		for (String line : lines) {
			line = line.trim();
			if (line.startsWith("mutation ") || line.startsWith("query ")) {
				// Extract the name of the operation after the keyword
				String[] parts = line.split("\\s+|\\(|\\{");
				if (parts.length > 1 && !parts[1].isBlank()) {
					return parts[1].trim();
				}
			}
		}

		return "unknown";
	}
}