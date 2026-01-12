package at.backend.MarketingCompany.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GraphQLAccessDeniedHandler implements AccessDeniedHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void handle(HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {

    response.setStatus(HttpStatus.FORBIDDEN.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("timestamp", LocalDateTime.now().toString());
    errorResponse.put("status", HttpStatus.FORBIDDEN.value());
    errorResponse.put("error", "Forbidden");
    errorResponse.put("message", accessDeniedException.getMessage());
    errorResponse.put("path", request.getRequestURI());

    // Para GraphQL
    if (request.getRequestURI().endsWith("/graphql")) {
      Map<String, Object> graphQLError = new HashMap<>();
      graphQLError.put("message", "Insufficient permissions");
      graphQLError.put("extensions", Map.of(
          "code", "FORBIDDEN",
          "httpStatus", 403));

      Map<String, Object> graphQLResponse = new HashMap<>();
      graphQLResponse.put("errors", List.of(graphQLError));
      graphQLResponse.put("data", null);

      objectMapper.writeValue(response.getWriter(), graphQLResponse);
    } else {
      objectMapper.writeValue(response.getWriter(), errorResponse);
    }
  }
}
