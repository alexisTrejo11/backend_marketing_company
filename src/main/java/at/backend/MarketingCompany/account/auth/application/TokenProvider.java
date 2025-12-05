package at.backend.MarketingCompany.account.auth.application;

import java.util.Set;

import at.backend.MarketingCompany.account.auth.domain.entitiy.valueobject.Role;
import io.jsonwebtoken.Claims;

public interface TokenProvider {
  String generateAccessToken(String userId, String email, Set<Role> roles);

  String generateRefreshToken();

  Claims validateToken(String token);

  String getUserIdFromToken(String token);

  boolean isTokenExpired(String token);
}
