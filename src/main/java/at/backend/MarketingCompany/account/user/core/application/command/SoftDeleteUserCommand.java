package at.backend.MarketingCompany.account.user.core.application.command;

import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.UserId;

public record SoftDeleteUserCommand(UserId userId) {
  public static SoftDeleteUserCommand from(String userId) {
    return new SoftDeleteUserCommand(
        new UserId(userId));
  }
}
