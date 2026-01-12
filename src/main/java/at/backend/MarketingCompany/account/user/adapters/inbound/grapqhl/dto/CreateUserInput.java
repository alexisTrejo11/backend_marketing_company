package at.backend.MarketingCompany.account.user.adapters.inbound.grapqhl.dto;

import java.time.LocalDate;

import at.backend.MarketingCompany.account.auth.core.domain.entitiy.valueobject.PlainPassword;
import at.backend.MarketingCompany.account.auth.core.domain.entitiy.valueobject.Role;
import at.backend.MarketingCompany.account.user.core.application.command.CreateUserCommand;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.PersonGender;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.PersonName;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.PersonalData;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.PhoneNumber;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record CreateUserInput(
    @NotBlank String firstName,
    @NotBlank String lastName,
    @NotNull PersonGender gender,
    @NotNull @PastOrPresent LocalDate dateOfBirth,
    @NotNull Role role,

    @NotBlank @Email String email,
    @NotBlank String password,
    String phoneNumber

) {
  public CreateUserCommand toCommand() {
    var personalData = new PersonalData(
        PersonName.from(firstName, lastName),
        dateOfBirth,
        gender);

    return CreateUserCommand.builder()
        .email(at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.Email.from(email))
        .phoneNumber(phoneNumber != null
            ? PhoneNumber.from(phoneNumber)
            : null)
        .password(PlainPassword.from(password))
        .personalData(personalData)
        .role(role)
        .build();
  }
}
