package at.backend.MarketingCompany.account.user.application.command;

import at.backend.MarketingCompany.account.user.domain.entity.valueobject.UserId;

public record DeactivateUserCommand(UserId userId) {
    public static DeactivateUserCommand from(String userId) {
        return new DeactivateUserCommand(
            new UserId(userId)
        );
    }
}
