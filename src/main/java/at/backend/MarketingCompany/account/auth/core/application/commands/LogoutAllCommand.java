package at.backend.MarketingCompany.account.auth.core.application.commands;

import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.UserId;

public record LogoutAllCommand(UserId userId) {
    public static  LogoutAllCommand from(String userId) {
        return new LogoutAllCommand(UserId.of(userId));
    }
}
