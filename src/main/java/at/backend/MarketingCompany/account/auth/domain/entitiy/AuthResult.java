package at.backend.MarketingCompany.account.auth.domain.entitiy;

import lombok.Getter;

import java.time.LocalDateTime;

import at.backend.MarketingCompany.account.user.domain.entity.User;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.UserId;

@Getter
public class AuthResult {
  private final UserId userId;
  private final String accessToken;
  private final String refreshToken;
  private final LocalDateTime accessTokenExpiresAt;
  private final LocalDateTime refreshTokenExpiresAt;
  private final User user;

  public AuthResult(UserId userId, String accessToken, String refreshToken,
      LocalDateTime accessTokenExpiresAt, LocalDateTime refreshTokenExpiresAt, User user) {
    this.userId = userId;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.accessTokenExpiresAt = accessTokenExpiresAt;
    this.refreshTokenExpiresAt = refreshTokenExpiresAt;
    this.user = user;
  }

  public boolean isValid() {
    return accessToken != null &&
        refreshToken != null &&
        accessTokenExpiresAt != null &&
        refreshTokenExpiresAt != null;
  }
}
