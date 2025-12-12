package at.backend.MarketingCompany.shared.graphql.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class WebFluxContextInterceptor implements WebGraphQlInterceptor {

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        Map<String, Object> contextMap = new HashMap<>();

        // User-Agent
        String userAgent = request.getHeaders().getFirst(HttpHeaders.USER_AGENT);
        contextMap.put("userAgent", userAgent != null ? userAgent : "GraphQL-Client");

        // Client IP
        String clientIp = resolveClientIp(request);
        contextMap.put("clientIp", clientIp != null ? clientIp : "unknown");

        // Request ID
        contextMap.put("requestId", request.getId());

        // URI info
        contextMap.put("path", request.getUri().getPath());

        log.debug("GraphQL WebFlux context - IP: {}, UA: {}, Path: {}",
                clientIp, userAgent, request.getUri().getPath());

        request.configureExecutionInput((executionInput, builder) ->
                builder.graphQLContext(contextMap).build()
        );

        return chain.next(request);
    }

    private String resolveClientIp(WebGraphQlRequest request) {
        InetSocketAddress remoteAddress = request.getRemoteAddress();
        if (remoteAddress != null) {
            return remoteAddress.getAddress().getHostAddress();
        }

        String xForwardedFor = request.getHeaders().getFirst("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeaders().getFirst("X-Real-IP");
        if (xRealIp != null && !xRealIp.isBlank()) {
            return xRealIp;
        }

        return "unknown";
    }
}