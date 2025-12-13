package at.backend.MarketingCompany.account.user.core.application.command;

import at.backend.MarketingCompany.account.auth.core.domain.entitiy.valueobject.HashedPassword;
import at.backend.MarketingCompany.account.auth.core.domain.entitiy.valueobject.PlainPassword;
import at.backend.MarketingCompany.account.auth.core.domain.entitiy.valueobject.Role;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.CreateUserParams;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.Email;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.PersonalData;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.PhoneNumber;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.UserStatus;
import lombok.Builder;

@Builder
public record CreateUserCommand(
    Email email,
    PhoneNumber phoneNumber,
    PlainPassword password,
    PersonalData personalData,
    Role role) {

  public CreateUserParams toCreateParams(String hashedPassword, UserStatus status) {
    return new CreateUserParams(
        email,
        phoneNumber,
        HashedPassword.from(hashedPassword),
        personalData,
        status);
  }
}
