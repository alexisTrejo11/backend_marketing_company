package at.backend.MarketingCompany.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GraphQLAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  @Override
  public void commence(HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {

    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("timestamp", LocalDateTime.now().toString());
    errorResponse.put("status", HttpStatus.UNAUTHORIZED.value());
    errorResponse.put("error", "Unauthorized");
    errorResponse.put("message", authException.getMessage());
    errorResponse.put("path", request.getRequestURI());

    if (request.getRequestURI().endsWith("/graphql")) {
      Map<String, Object> graphQLError = new HashMap<>();
      graphQLError.put("message", "Authentication required");
      graphQLError.put("extensions", Map.of(
          "code", "UNAUTHENTICATED",
          "httpStatus", 401));

      Map<String, Object> graphQLResponse = new HashMap<>();
      graphQLResponse.put("errors", List.of(graphQLError));
      graphQLResponse.put("data", null);

      objectMapper.writeValue(response.getWriter(), graphQLResponse);
    } else {
      objectMapper.writeValue(response.getWriter(), errorResponse);
    }
  }
}
