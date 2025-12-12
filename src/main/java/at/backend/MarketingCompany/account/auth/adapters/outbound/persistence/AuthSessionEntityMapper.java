package at.backend.MarketingCompany.account.auth.adapters.outbound.persistence;

import at.backend.MarketingCompany.account.auth.core.domain.entitiy.AuthSession;
import at.backend.MarketingCompany.account.auth.core.domain.entitiy.valueobject.SessionId;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.UserId;
import org.springframework.stereotype.Component;

@Component
public class AuthSessionEntityMapper {

    public AuthSessionEntity toEntity(AuthSession domain) {
        if (domain == null) return null;

        return AuthSessionEntity.builder()
                .sessionId(domain.getSessionId().value())
                .userId(domain.getUserId().value())
                .createdAt(domain.getCreatedAt())
                .expiresAt(domain.getExpiresAt())
                .lastAccessedAt(domain.getLastAccessedAt())
                .userAgent(domain.getUserAgent())
                .ipAddress(domain.getIpAddress())
                .revoked(domain.isRevoked())
                .ttl(domain.getRemainingTTLMinutes())
                .build();
    }

    public AuthSession toDomain(AuthSessionEntity entity) {
        if (entity == null) return null;

        return AuthSession.reconstruct(
                SessionId.from(entity.getSessionId()),
                UserId.from(entity.getUserId()),
                entity.getCreatedAt(),
                entity.getExpiresAt(),
                entity.getLastAccessedAt(),
                entity.getUserAgent(),
                entity.getIpAddress(),
                entity.isRevoked()
        );
    }
}