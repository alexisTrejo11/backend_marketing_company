package at.backend.MarketingCompany.account.auth.application.commands;

import at.backend.MarketingCompany.account.auth.domain.entitiy.valueobject.SessionId;

public record LogoutCommand(
    SessionId sessionId) {

    public static LogoutCommand from(String sessionId) {
        return new LogoutCommand(
            new SessionId(sessionId)
        );
    }
}
