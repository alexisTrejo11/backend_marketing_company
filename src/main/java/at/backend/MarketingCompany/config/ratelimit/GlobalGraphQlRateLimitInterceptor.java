package at.backend.MarketingCompany.config.ratelimit;

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
@Order(1)
@Slf4j
@RequiredArgsConstructor
public class GlobalGraphQlRateLimitInterceptor implements WebGraphQlInterceptor {

	private final RedisRateLimiter rateLimiter;
	private final RateLimitProperties properties;

	@Override
	public @NotNull Mono<WebGraphQlResponse> intercept(@NotNull WebGraphQlRequest request, @NotNull Chain chain) {
		if (!properties.getGlobal().isEnabled()) {
			return chain.next(request);
		}

		String clientIp = getClientIp(request);
		String globalKey = "global:ip:" + clientIp;
		Duration duration = Duration.ofHours(properties.getGlobal().getDurationHours());
		int maxRequests = properties.getGlobal().getMaxRequests();

		// Get Info before processing the request
		RedisRateLimiter.RateLimitInfo info = rateLimiter.getRateLimitInfo(globalKey, maxRequests);
		boolean isAllowed = rateLimiter.isAllowed(globalKey, maxRequests, duration);

		if (!isAllowed) {
			log.warn("Global rate limit exceeded for IP: {}", clientIp);
			return Mono.error(new RuntimeException(
					String.format("Global rate limit exceeded. Try again in %d seconds", info.getResetAfter())
			));
		}

		return chain.next(request).map(response -> {
			// Update Info after processing the request
			RedisRateLimiter.RateLimitInfo updatedInfo = rateLimiter.getRateLimitInfo(globalKey, maxRequests);

			// Add Rate Limit Headers
			HttpHeaders headers = response.getResponseHeaders();
			headers.add("X-RateLimit-Global-Limit", String.valueOf(maxRequests));
			headers.add("X-RateLimit-Global-Remaining", String.valueOf(updatedInfo.getRemaining()));
			headers.add("X-RateLimit-Global-Reset", String.valueOf(updatedInfo.getResetAfter()));
			headers.add("X-RateLimit-Global-Window", properties.getGlobal().getDurationHours() + " hours");

			return response;
		});
	}

	private String getClientIp(WebGraphQlRequest request) {
		List<String> xfHeader = request.getHeaders().get("X-Forwarded-For");
		if (xfHeader != null && !xfHeader.isEmpty()) {
			return xfHeader.getFirst().split(",")[0].trim();
		}
		return request.getRemoteAddress() != null ?
				request.getRemoteAddress().getAddress().getHostAddress() : "unknown";
	}
}