package at.backend.MarketingCompany.config.security;

import at.backend.MarketingCompany.account.auth.core.application.AuthCommandHandler;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.FieldCoordinates;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLFieldsContainer;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthenticationDirective implements SchemaDirectiveWiring {
  private final AuthCommandHandler authCommandHandler;

  public AuthenticationDirective(AuthCommandHandler authCommandHandler) {
    this.authCommandHandler = authCommandHandler;
  }

  @Override
  public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> environment) {
    GraphQLFieldDefinition field = environment.getElement();
    GraphQLFieldsContainer parentType = environment.getFieldsContainer();

    FieldCoordinates coordinates = FieldCoordinates.coordinates(parentType, field);
    DataFetcher<?> originalDataFetcher = environment.getCodeRegistry().getDataFetcher(coordinates, field);
    DataFetcher<?> authDataFetcher = buildDataFetcher(originalDataFetcher, field.getName());

    environment.getCodeRegistry().dataFetcher(coordinates, authDataFetcher);
    return field;
  }

  private DataFetcher<?> buildDataFetcher(DataFetcher<?> original, String fieldName) {
    return (DataFetchingEnvironment dataFetchingEnvironment) -> {
      // Extraer token del contexto GraphQL
      String token = extractToken(dataFetchingEnvironment);

      if (token == null) {
        throw new AccessDeniedException("Authentication required for field: " + fieldName);
      }

      try {
        Claims claims = authCommandHandler.getAccessTokenClaims(token);

        String userId = claims.getSubject();
        String username = claims.get("username", String.class);
        List<String> roles = claims.get("roles", List.class);

        boolean sessionActive = isSessionActive(userId, token);

        if (!sessionActive) {
          throw new AccessDeniedException("Session is not active or has been revoked");
        }

        // Add user info to GraphQL context
        dataFetchingEnvironment.getGraphQlContext().put("userId", userId);
        dataFetchingEnvironment.getGraphQlContext().put("username", username);
        dataFetchingEnvironment.getGraphQlContext().put("roles", roles);
        dataFetchingEnvironment.getGraphQlContext().put("claims", claims);

      } catch (Exception e) {
        throw new AccessDeniedException("Invalid authentication: " + e.getMessage());
      }

      // Continue with the original data fetcher
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

  private boolean isSessionActive(String userId, String accessToken) {
    // TODO: Implement session verification logic
    return true;
  }
}
