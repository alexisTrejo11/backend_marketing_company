package at.backend.MarketingCompany.account.auth.adapaters.outbound.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import at.backend.MarketingCompany.account.auth.application.TokenProvider;
import at.backend.MarketingCompany.account.auth.domain.entitiy.valueobject.Role;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Component
public class TokenProviderImpl implements TokenProvider {
  @Value("${app.auth.jwt-secret:defaultSecretKeyThatShouldBeChangedInProduction}")
  private String jwtSecret;

  @Value("${app.auth.jwt-expiration-minutes:15}")
  private int jwtExpirationMinutes;

  private SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(jwtSecret.getBytes());
  }

  public String generateAccessToken(String userId, String email, Set<Role> roles) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtExpirationMinutes * 60 * 1000);

    return Jwts.builder()
        .subject(userId)
        .claim("email", email)
        .claim("roles", roles.stream().map(Enum::name).toList())
        .issuedAt(now)
        .expiration(expiryDate)
        .signWith(getSigningKey())
        .compact();
  }

  public String generateRefreshToken() {
    return UUID.randomUUID().toString();
  }

  public Claims validateToken(String token) {
    return Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  public String getUserIdFromToken(String token) {
    Claims claims = validateToken(token);
    return claims.getSubject();
  }

  public boolean isTokenExpired(String token) {
    try {
      Claims claims = validateToken(token);
      Date expiration = claims.getExpiration();
      return expiration.before(new Date());
    } catch (Exception e) {
      return true;
    }
  }
}
