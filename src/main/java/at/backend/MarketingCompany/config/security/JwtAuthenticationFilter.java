package at.backend.MarketingCompany.config.security;

import at.backend.MarketingCompany.account.auth.core.application.AuthCommandHandler;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final AuthCommandHandler authCommandHandler;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {

    try {
      String token = extractToken(request);

      if (token != null) {
        Claims claims = authCommandHandler.getAccessTokenClaims(token);

        if (claims != null) {
          boolean sessionActive = verifySessionActive(claims.getSubject(), token);

          if (sessionActive) {
            String username = claims.get("username", String.class);
            List<String> roles = claims.get("roles", List.class);

            Collection<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null,
                authorities);

            authentication.setDetails(claims);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Add user info to request for GraphQL
            request.setAttribute("userId", claims.getSubject());
            request.setAttribute("username", username);
            request.setAttribute("userRoles", roles);
            request.setAttribute("claims", claims);
          }
        }
      }
    } catch (Exception e) {
      // Log error pero continuar sin autenticación
      logger.debug("JWT authentication failed: " + e.getMessage());
    }

    filterChain.doFilter(request, response);
  }

  private String extractToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  private boolean verifySessionActive(String userId, String token) {
    // Aquí puedes implementar lógica adicional para verificar sesión activa
    // Por ejemplo, consultar la base de datos o Redis
    // Por ahora, asumimos válido si el JWT es válido
    return true;
  }
}
