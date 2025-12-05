package at.backend.MarketingCompany.account.auth.adapaters.inbound.dto.output;

import at.backend.MarketingCompany.account.user.adapters.inbound.grapqhl.dto.UserResponse;

import java.time.LocalDateTime;

public record AuthResponse(
    String accessToken,
    String refreshToken,
    LocalDateTime accessTokenExpiresAt,
    LocalDateTime refreshTokenExpiresAt,
    UserResponse user
) {}