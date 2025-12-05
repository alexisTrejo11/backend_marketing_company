package at.backend.MarketingCompany.account.user.application.command;

import at.backend.MarketingCompany.account.user.domain.entity.valueobject.UserId;

public record ActivateUserCommand(UserId userId) {

    public static ActivateUserCommand from(String userId) {
        return new ActivateUserCommand(
            new UserId(userId)
        );
    }
}
