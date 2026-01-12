package at.backend.MarketingCompany.account.auth.core.application;

import java.util.Set;

import at.backend.MarketingCompany.account.auth.core.domain.entitiy.valueobject.Role;
import io.jsonwebtoken.Claims;

public interface TokenProvider {
    String generateAccessToken(String userId, String email, Set<Role> roles);

    String generateRefreshToken(String userId);

    Claims validateAccessToken(String token);

    Claims validateRefreshToken(String token);

    String getUserIdFromToken(String token, boolean isRefreshToken);

    String getTokenIdFromRefreshToken(String refreshToken);

    String getSessionIdFromRefreshToken(String refreshToken);

    boolean isTokenExpired(String token, boolean isRefreshToken);
}
