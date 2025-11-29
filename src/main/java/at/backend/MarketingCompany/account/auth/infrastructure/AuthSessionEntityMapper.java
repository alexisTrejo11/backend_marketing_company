package at.backend.MarketingCompany.account.auth.infrastructure;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import at.backend.MarketingCompany.account.auth.domain.entitiy.AuthSession;
import at.backend.MarketingCompany.account.auth.domain.entitiy.valueobject.SessionId;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.UserId;

@Component
public class AuthSessionEntityMapper {

  public AuthSessionEntity toEntity(AuthSession session) {
    if (session == null)
      return null;

    AuthSessionEntity entity = AuthSessionEntity.builder()
        .sessionId(session.getSessionId().value())
        .userId(session.getUserId().value())
        .refreshToken(session.getRefreshToken())
        .createdAt(session.getCreatedAt())
        .expiresAt(session.getExpiresAt())
        .lastAccessedAt(session.getLastAccessedAt())
        .userAgent(session.getUserAgent())
        .ipAddress(session.getIpAddress())
        .revoked(session.isRevoked())
        .build();

    entity.calculateTTL();

    return entity;
  }

  public AuthSession toDomain(AuthSessionEntity entity) {
    if (entity == null)
      return null;

    // Usar el factory method de AuthSession
    var session = AuthSession.create(
        UserId.from(entity.getUserId()),
        entity.getRefreshToken(),
        calculateDaysUntilExpiry(entity.getExpiresAt()),
        entity.getUserAgent(),
        entity.getIpAddress());

    var sessionReflection = session;
    try {
      var sessionIdField = AuthSession.class.getDeclaredField("sessionId");
      sessionIdField.setAccessible(true);
      sessionIdField.set(sessionReflection, SessionId.from(entity.getSessionId()));

      var createdAtField = AuthSession.class.getDeclaredField("createdAt");
      createdAtField.setAccessible(true);
      createdAtField.set(sessionReflection, entity.getCreatedAt());

      var lastAccessedAtField = AuthSession.class.getDeclaredField("lastAccessedAt");
      lastAccessedAtField.setAccessible(true);
      lastAccessedAtField.set(sessionReflection, entity.getLastAccessedAt());

      var revokedField = AuthSession.class.getDeclaredField("revoked");
      revokedField.setAccessible(true);
      revokedField.set(sessionReflection, entity.isRevoked());

    } catch (Exception e) {
      throw new RuntimeException("Error mapping AuthSession entity to domain", e);
    }

    return sessionReflection;
  }

  private int calculateDaysUntilExpiry(LocalDateTime expiresAt) {
    if (expiresAt == null)
      return 30; // Default 30 days

    long days = java.time.Duration.between(LocalDateTime.now(), expiresAt).toDays();
    return Math.max(1, (int) days); // At least 1 day
  }
}
