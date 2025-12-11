package at.backend.MarketingCompany.shared.graphql.logging;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@Slf4j
@Component
public class GraphQLLoggingInterceptor implements WebGraphQlInterceptor {

    @Override
    public @NotNull Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        Instant start = Instant.now();
        String operationName = request.getOperationName();
        Map<String, Object> variables = request.getVariables();

        log.info("GraphQL request started - Operation: {}, ID: {}",
                operationName, request.getId());

        return chain.next(request)
                .doOnNext(response -> {
                    Duration duration = Duration.between(start, Instant.now());

                    response.getErrors();
                    if (!response.getErrors().isEmpty()) {
                        log.error("GraphQL request completed with {} errors in {} ms - Operation: {}",
                                response.getErrors().size(), duration.toMillis(), operationName);
                    } else {
                        log.info("GraphQL request completed successfully in {} ms - Operation: {}",
                                duration.toMillis(), operationName);
                    }
                })
                .doOnError(error -> {
                    Duration duration = Duration.between(start, Instant.now());
                    log.error("GraphQL request failed after {} ms - Operation: {}",
                            duration.toMillis(), operationName, error);
                });
    }
}