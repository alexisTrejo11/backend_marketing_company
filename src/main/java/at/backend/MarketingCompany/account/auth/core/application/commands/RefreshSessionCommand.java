package at.backend.MarketingCompany.account.auth.core.application.commands;

public record RefreshSessionCommand(
    String refreshToken,
    String userAgent,
    String ipAddress) {
}
