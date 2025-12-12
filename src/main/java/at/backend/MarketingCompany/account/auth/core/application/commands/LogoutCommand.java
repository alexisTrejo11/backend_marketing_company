package at.backend.MarketingCompany.account.auth.core.application.commands;

public record LogoutCommand(String sessionId) {

    public static LogoutCommand from(String refreshToken) {
        return new LogoutCommand(refreshToken);
    }
}
