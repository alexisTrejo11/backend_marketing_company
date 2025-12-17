package at.backend.MarketingCompany.account.user.core.domain.exceptions;

import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.Email;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.UserId;
import at.backend.MarketingCompany.shared.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
  public UserNotFoundException(UserId userId) {
    super("User", userId.asString());
  }

  public UserNotFoundException(Email email) {
    super("User", email.toString());
  }
}
