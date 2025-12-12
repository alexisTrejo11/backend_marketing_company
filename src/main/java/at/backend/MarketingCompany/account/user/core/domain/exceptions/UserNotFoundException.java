package at.backend.MarketingCompany.account.user.core.domain.exceptions;

import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.Email;
import at.backend.MarketingCompany.shared.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
  public UserNotFoundException(String userId) {
    super("User", userId);
  }

  public UserNotFoundException(Email email) {
    super("User", email.toString());
  }
}
