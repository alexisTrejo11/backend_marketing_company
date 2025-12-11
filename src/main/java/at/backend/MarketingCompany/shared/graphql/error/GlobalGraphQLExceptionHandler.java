package at.backend.MarketingCompany.shared.graphql.error;

import at.backend.MarketingCompany.shared.exception.*;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@ControllerAdvice
public class GlobalGraphQLExceptionHandler extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(@NotNull Throwable ex, DataFetchingEnvironment env) {
        log.error("GraphQL error occurred at path: {}", env.getExecutionStepInfo().getPath(), ex);

        switch (ex) {
            case NotFoundException notFoundException -> {
                return buildGraphQLError(
                        notFoundException.getMessage(),
                        ErrorType.NOT_FOUND,
                        env,
                        Map.of("errorCode", notFoundException.getErrorCode(),
                                "details", notFoundException.getDetails())
                );
            }
            case ValidationException validationException -> {
                return buildGraphQLError(
                        validationException.getMessage(),
                        ErrorType.BAD_REQUEST,
                        env,
                        Map.of("errorCode", validationException.getErrorCode(),
                                "details", validationException.getDetails())
                );
            }
            case BusinessRuleException businessRuleException -> {
                return buildGraphQLError(
                        businessRuleException.getMessage(),
                        ErrorType.BAD_REQUEST,
                        env,
                        Map.of("errorCode", businessRuleException.getErrorCode(),
                                "ruleCode", businessRuleException.getDetails().get("ruleCode"))
                );
            }
            case ConstraintViolationException constraintException -> {
                Map<String, Object> validationErrors = constraintException.getConstraintViolations()
                        .stream()
                        .collect(Collectors.toMap(
                                violation -> violation.getPropertyPath().toString(),
                                ConstraintViolation::getMessage
                        ));

                return buildGraphQLError(
                        "Validation failed",
                        ErrorType.BAD_REQUEST,
                        env,
                        Map.of("errorCode", "VALIDATION_ERROR",
                                "validationErrors", validationErrors)
                );
            }
            case IllegalArgumentException illegalArgumentException -> {
                return buildGraphQLError(
                        illegalArgumentException.getMessage(),
                        ErrorType.BAD_REQUEST,
                        env,
                        Map.of("errorCode", "INVALID_INPUT")
                );
            }
            default -> {
                log.error("Unhandled exception type: {}", ex.getClass().getName());
            }
        }
        return buildGraphQLError(
                "Internal server error",
                ErrorType.INTERNAL_ERROR,
                env,
                Map.of("errorCode", "INTERNAL_ERROR",
                        "timestamp", Instant.now().toString())
        );
    }

    private GraphQLError buildGraphQLError(
            String message,
            ErrorType errorType,
            DataFetchingEnvironment env,
            Map<String, Object> extensions
    ) {
        return GraphqlErrorBuilder.newError()
                .message(message)
                .errorType(errorType)
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .extensions(extensions)
                .build();
    }
}