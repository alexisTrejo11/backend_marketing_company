package at.backend.MarketingCompany.account.user.core.application.command;

import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.UserId;

public record RestoreUserCommand(UserId userId) {
  public static RestoreUserCommand from(String userId) {
    return new RestoreUserCommand(
        UserId.of(userId));
  }
}
