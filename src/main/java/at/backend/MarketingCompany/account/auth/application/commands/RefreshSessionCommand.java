package at.backend.MarketingCompany.account.auth.application.commands;

public record RefreshSessionCommand(
    String refreshToken,
    String userAgent,
    String ipAddress) {
}
