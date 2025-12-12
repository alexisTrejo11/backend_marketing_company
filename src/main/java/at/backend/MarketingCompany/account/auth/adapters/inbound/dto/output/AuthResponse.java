package at.backend.MarketingCompany.account.auth.adapters.inbound.dto.output;

import at.backend.MarketingCompany.account.user.adapters.inbound.grapqhl.dto.UserResponse;

import java.time.OffsetDateTime;

public record AuthResponse(
    String accessToken,
    String refreshToken,
    OffsetDateTime accessTokenExpiresAt,
    OffsetDateTime refreshTokenExpiresAt,
    UserResponse user
) {}