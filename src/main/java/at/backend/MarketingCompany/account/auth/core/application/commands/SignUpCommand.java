package at.backend.MarketingCompany.account.auth.core.application.commands;

import java.time.LocalDate;

import at.backend.MarketingCompany.account.auth.core.domain.entitiy.valueobject.PlainPassword;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.Email;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.PersonGender;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.PersonName;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.PersonalData;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.PhoneNumber;

public record SignUpCommand(
    Email email,
    PlainPassword password,
    PersonalData personalData,
    PhoneNumber phoneNumber,
    String userAgent,
    String ipAddress) {

  public static SignUpCommand of(
      String email,
      String password,
      String firstName,
      String lastName,
      LocalDate dateOfBirth,
      PersonGender gender,
      String phoneNumber,
      String userAgent,
      String ipAddress) {

    PersonalData personalData = new PersonalData(
        PersonName.from(firstName, lastName),
        dateOfBirth,
        gender);

    return new SignUpCommand(
        Email.from(email),
        PlainPassword.from(password),
        personalData,
        PhoneNumber.from(phoneNumber),
        userAgent,
        ipAddress);
  }
}
