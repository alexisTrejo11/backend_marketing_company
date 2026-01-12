package at.backend.MarketingCompany.config.security;

import at.backend.MarketingCompany.account.auth.core.application.AuthCommandHandler;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLFieldsContainer;
import graphql.schema.GraphQLObjectType;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoleDirective implements SchemaDirectiveWiring {

  private final AuthCommandHandler authCommandHandler;

  public RoleDirective(AuthCommandHandler authCommandHandler) {
    this.authCommandHandler = authCommandHandler;
  }

  @Override
  public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> environment) {
    GraphQLFieldDefinition field = environment.getElement();
    List<String> requiredRoles = (List<String>) environment.getAppliedDirective("role").getArgument("value").getValue();

    GraphQLFieldsContainer parentType = environment.getFieldsContainer();
    DataFetcher<?> originalDataFetcher = environment.getCodeRegistry().getDataFetcher((GraphQLObjectType) parentType,
        field);

    DataFetcher<?> authDataFetcher = buildDataFetcher(originalDataFetcher, field.getName(), requiredRoles);

    environment.getCodeRegistry().dataFetcher((GraphQLObjectType) parentType, field, authDataFetcher);
    return field;
  }

  private DataFetcher<?> buildDataFetcher(DataFetcher<?> original, String fieldName, List<String> requiredRoles) {
    return (DataFetchingEnvironment dataFetchingEnvironment) -> {
      String token = extractToken(dataFetchingEnvironment);

      if (token == null) {
        throw new AccessDeniedException("Authentication required for field: " + fieldName);
      }

      try {
        Claims claims = authCommandHandler.getAccessTokenClaims(token);
        List<String> userRoles = claims.get("roles", List.class);

        // Verificar si el usuario tiene al menos uno de los roles requeridos
        boolean hasRequiredRole = userRoles.stream()
            .anyMatch(requiredRoles::contains);

        if (!hasRequiredRole) {
          String userRoleStr = userRoles.stream().collect(Collectors.joining(", "));
          String requiredRoleStr = String.join(", ", requiredRoles);
          throw new AccessDeniedException(
              String.format("Insufficient permissions. User roles: [%s]. Required roles: [%s]",
                  userRoleStr, requiredRoleStr));
        }

      } catch (Exception e) {
        throw new AccessDeniedException("Authorization error: " + e.getMessage());
      }

      return original.get(dataFetchingEnvironment);
    };
  }

  private String extractToken(DataFetchingEnvironment env) {
    HttpServletRequest request = env.getGraphQlContext().get("httpServletRequest");
    if (request != null) {
      String bearerToken = request.getHeader("Authorization");
      if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
        return bearerToken.substring(7);
      }
    }
    return null;
  }
}
