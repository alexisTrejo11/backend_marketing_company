package at.backend.MarketingCompany.account.auth.application.commands;

public record RefreshTokenCommand(
    String refreshToken,
    String userAgent,
    String ipAddress) {
}
