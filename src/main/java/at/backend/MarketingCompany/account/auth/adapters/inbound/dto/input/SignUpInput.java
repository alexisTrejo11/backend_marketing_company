package at.backend.MarketingCompany.account.auth.adapters.inbound.dto.input;

import java.time.LocalDate;

import at.backend.MarketingCompany.account.auth.core.application.commands.SignUpCommand;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.PersonGender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record SignUpInput(
    @NotBlank @Email String email,
    @NotBlank String password,
    @NotBlank String firstName,
    @NotBlank String lastName,
    @NotNull @PastOrPresent LocalDate dateOfBirth,
    @NotNull PersonGender gender,
    String phoneNumber) {

  public SignUpCommand toCommand(String userAgent, String ipAddress) {
    return SignUpCommand.of(
        email,
        password,
        firstName,
        lastName,
        dateOfBirth,
        gender,
        phoneNumber,
        userAgent,
        ipAddress);
  }
}
