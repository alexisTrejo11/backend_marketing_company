package at.backend.MarketingCompany.config.ratelimit;

import at.backend.MarketingCompany.config.ratelimit.base.GraphQLRateLimit;
import at.backend.MarketingCompany.config.ratelimit.base.RateLimitExceededException;
import at.backend.MarketingCompany.config.ratelimit.base.RateLimitType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Duration;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class GraphQLRateLimitAspect {

	private final RedisRateLimiter rateLimiter;
	private final RateLimitProperties properties;

	// SOLUCIÓN: Interceptar directamente métodos con @GraphQLRateLimit
	@Around("@annotation(rateLimit)")
	public Object applyRateLimit(ProceedingJoinPoint joinPoint, GraphQLRateLimit rateLimit) throws Throwable {
		log.info("=== RATE LIMIT ASPECT TRIGGERED ===");

		String operationName = joinPoint.getSignature().getName();
		String profile = rateLimit.value();

		log.info("Applying rate limit to method: {} with profile: {}", operationName, profile);

		// Obtener configuración
		int maxRequests;
		int durationSeconds;

		if (properties.getEndpoints().containsKey(profile)) {
			RateLimitProperties.EndpointConfig endpointConfig = properties.getEndpoints().get(profile);
			maxRequests = endpointConfig.getMaxRequests();
			durationSeconds = endpointConfig.getDurationSeconds();
			log.info("Using endpoint config for '{}': {} req/{}s", profile, maxRequests, durationSeconds);
		} else {
			maxRequests = properties.getDefaults().getMaxRequests();
			durationSeconds = properties.getDefaults().getDurationSeconds();
			log.warn("Profile '{}' not found, using default: {} req/{}s", profile, maxRequests, durationSeconds);
		}

		// Construir key
		String key = buildKey(operationName, rateLimit.type());
		log.info("Rate limit key: {}, checking...", key);

		// Verificar rate limit
		if (!rateLimiter.isAllowed(key, maxRequests, Duration.ofSeconds(durationSeconds))) {
			RedisRateLimiter.RateLimitInfo info = rateLimiter.getRateLimitInfo(key, maxRequests);
			log.warn("Rate limit EXCEEDED for {} - Profile: {}, Remaining: {}, Reset in: {}s",
					operationName, profile, info.getRemaining(), info.getResetAfter());

			throw new RateLimitExceededException(
					String.format("Rate limit exceeded for operation '%s'. Try again in %d seconds.",
							operationName, info.getResetAfter())
			);
		}

		// Ejecutar el método original
		Object result = joinPoint.proceed();

		// Obtener info actualizada después de la ejecución
		RedisRateLimiter.RateLimitInfo updatedInfo = rateLimiter.getRateLimitInfo(key, maxRequests);
		log.info("Rate limit OK for {} - Remaining: {}, Reset in: {}s",
				operationName, updatedInfo.getRemaining(), updatedInfo.getResetAfter());

		// Agregar headers a la respuesta
		addRateLimitHeaders(updatedInfo, maxRequests, durationSeconds);

		return result;
	}

	private void addRateLimitHeaders(RedisRateLimiter.RateLimitInfo info,
	                                 int maxRequests,
	                                 int durationSeconds) {
		ServletRequestAttributes attrs = (ServletRequestAttributes)
				RequestContextHolder.getRequestAttributes();

		if (attrs != null && attrs.getResponse() != null) {
			var response = attrs.getResponse();
			response.setHeader("X-RateLimit-Operation-Limit", String.valueOf(maxRequests));
			response.setHeader("X-RateLimit-Operation-Remaining", String.valueOf(info.getRemaining()));
			response.setHeader("X-RateLimit-Operation-Reset", String.valueOf(info.getResetAfter()));
			response.setHeader("X-RateLimit-Operation-Window", durationSeconds + " seconds");

			log.debug("Added operation rate limit headers");
		}
	}

	private String buildKey(String operationName, RateLimitType type) {
		ServletRequestAttributes attrs = (ServletRequestAttributes)
				RequestContextHolder.getRequestAttributes();

		if (attrs == null) {
			log.warn("No request attributes found, using fallback key");
			return String.format("graphql:%s:no-context", operationName);
		}

		HttpServletRequest request = attrs.getRequest();

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

	private String getClientIp(HttpServletRequest request) {
		String xfHeader = request.getHeader("X-Forwarded-For");
		if (xfHeader != null && !xfHeader.isEmpty()) {
			return xfHeader.split(",")[0].trim();
		}
		return request.getRemoteAddr();
	}

	private String getUserId(HttpServletRequest request) {
		// Implementa según tu auth system
		return "anonymous";
	}
}