package at.backend.MarketingCompany.account.auth.adapters.inbound.dto.output;

import java.time.OffsetDateTime;

public record SessionResponse(
    String sessionId,
    OffsetDateTime createdAt,
    OffsetDateTime expiresAt,
    OffsetDateTime lastAccessedAt,
    String userAgent,
    String ipAddress,
    boolean valid
) {}