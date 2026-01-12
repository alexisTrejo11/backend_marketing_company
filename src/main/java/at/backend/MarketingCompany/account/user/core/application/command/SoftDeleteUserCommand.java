package at.backend.MarketingCompany.account.user.core.application.command;

import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.UserId;

public record SoftDeleteUserCommand(UserId userId, boolean isUserAction) {
	public static SoftDeleteUserCommand from(String userId, boolean isUserAction) {
		return new SoftDeleteUserCommand(UserId.of(userId), isUserAction);
	}
}
