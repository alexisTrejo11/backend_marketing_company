package at.backend.MarketingCompany.account.auth.adapaters.inbound.dto.output;

import java.time.LocalDateTime;

public record SessionResponse(
    String sessionId,
    LocalDateTime createdAt,
    LocalDateTime expiresAt,
    LocalDateTime lastAccessedAt,
    String userAgent,
    String ipAddress,
    boolean valid
) {}