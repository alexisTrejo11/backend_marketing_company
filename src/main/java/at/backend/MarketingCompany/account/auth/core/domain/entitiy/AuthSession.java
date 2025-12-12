package at.backend.MarketingCompany.account.auth.core.domain.entitiy;

import at.backend.MarketingCompany.account.auth.core.domain.entitiy.valueobject.SessionId;
import at.backend.MarketingCompany.account.auth.core.domain.exceptions.AuthValidationException;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.UserId;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AuthSession {
    private SessionId sessionId;
    private UserId userId;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime lastAccessedAt;
    private String userAgent;
    private String ipAddress;
    private boolean revoked;


    public AuthSession(SessionId sessionId, UserId userId,
                       LocalDateTime expiresAt, String userAgent, String ipAddress) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
        this.expiresAt = expiresAt;
        this.lastAccessedAt = LocalDateTime.now();
        this.userAgent = userAgent;
        this.ipAddress = ipAddress;
        this.revoked = false;
        validateState();
    }

    public static AuthSession reconstruct(SessionId sessionId, UserId userId,
                                      LocalDateTime createdAt, LocalDateTime expiresAt,
                                      LocalDateTime lastAccessedAt, String userAgent,
                                      String ipAddress, boolean revoked) {
        AuthSession session = new AuthSession(sessionId, userId, expiresAt, userAgent, ipAddress);
        session.createdAt = createdAt;
        session.lastAccessedAt = lastAccessedAt;
        session.revoked = revoked;
        session.validateState();
        return session;
    }

    public static AuthSession create(UserId userId, String refreshToken,
                                     int refreshTokenExpiryDays, String userAgent, String ipAddress) {
        if (userId == null) {
            throw new AuthValidationException("User ID is required");
        }
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new AuthValidationException("Refresh token is required");
        }

        var sessionId = SessionId.from(refreshToken);
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(refreshTokenExpiryDays);

        return new AuthSession(sessionId, userId, expiresAt, userAgent, ipAddress);
    }

    public void revoke() {
        this.revoked = true;
    }

    public void updateAccess() {
        this.lastAccessedAt = LocalDateTime.now();
    }

    public boolean isValid() {
        return !isExpired() && !isRevoked();
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public long getRemainingTTLMinutes() {
        return java.time.Duration.between(LocalDateTime.now(), expiresAt).toMinutes();
    }

    private void validateState() {
        if (sessionId == null) {
            throw new AuthValidationException("Session ID is required");
        }
        if (userId == null) {
            throw new AuthValidationException("User ID is required");
        }
        if (expiresAt == null) {
            throw new AuthValidationException("Expiration date is required");
        }
        if (expiresAt.isBefore(LocalDateTime.now())) {
            throw new AuthValidationException("Session cannot be created with past expiration date");
        }
    }
}
