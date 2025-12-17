package at.backend.MarketingCompany.account.user.core.application.command;

import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.UserId;

public record UpdateUserStatusCommand(UserId userId) {

  public static UpdateUserStatusCommand from(String userId) {
    return new UpdateUserStatusCommand(
        UserId.of(userId));
  }
}
