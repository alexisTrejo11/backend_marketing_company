package at.backend.MarketingCompany.config.security;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import reactor.core.publisher.Mono;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Interceptor to transfer authentication data from SecurityContext and
 * ServletRequest
 * to GraphQL context.
 */
@Slf4j
@Component
public class GraphQLSecurityInterceptor implements WebGraphQlInterceptor {

  @Override
  public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
    Map<String, Object> contextMap = new HashMap<>();

    try {
      // Get HttpServletRequest from RequestContextHolder
      ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

      if (attributes != null) {
        HttpServletRequest httpRequest = attributes.getRequest();

        // Get userId and other attributes from request
        Object userId = httpRequest.getAttribute("userId");
        Object username = httpRequest.getAttribute("username");
        Object userRoles = httpRequest.getAttribute("userRoles");
        Object claims = httpRequest.getAttribute("claims");

        if (userId != null) {
          contextMap.put("userId", userId);
          log.debug("GraphQL context - userId: {}", userId);
        }

        if (username != null) {
          contextMap.put("username", username);
        }

        if (userRoles != null) {
          contextMap.put("userRoles", userRoles);
          log.debug("GraphQL context - userRoles: {}", userRoles);
        }

        if (claims != null) {
          contextMap.put("claims", claims);
        }

        // Also add the HttpServletRequest itself for directive access
        contextMap.put("httpServletRequest", httpRequest);
      }

      // Also check SecurityContext as fallback
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication != null && authentication.isAuthenticated()) {
        if (!contextMap.containsKey("username")) {
          contextMap.put("username", authentication.getName());
        }

        if (!contextMap.containsKey("userRoles")) {
          List<String> roles = authentication.getAuthorities().stream()
              .map(GrantedAuthority::getAuthority)
              .collect(Collectors.toList());
          contextMap.put("userRoles", roles);
        }

        // Extract userId from JWT claims if available
        if (authentication.getDetails() instanceof Claims && !contextMap.containsKey("userId")) {
          Claims jwtClaims = (Claims) authentication.getDetails();
          contextMap.put("userId", jwtClaims.getSubject());
          contextMap.put("claims", jwtClaims);
        }
      }

    } catch (Exception e) {
      log.warn("Error transferring authentication to GraphQL context: {}", e.getMessage());
    }

    // Configure execution input with the context map
    if (!contextMap.isEmpty()) {
      request.configureExecutionInput((executionInput, builder) -> builder.graphQLContext(contextMap).build());
    }

    return chain.next(request);
  }
}
