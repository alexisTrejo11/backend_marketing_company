package at.backend.MarketingCompany.account.user.adapters.inbound.grapqhl.dto;

import java.time.LocalDate;

import at.backend.MarketingCompany.account.user.core.application.command.UpdateUserPersonalDataCommand;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.PersonGender;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.PersonName;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.UserId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record UpdatePersonalDataInput(
    @NotBlank String firstName,
    @NotBlank String lastName,
    @PastOrPresent LocalDate dateOfBirth,
    @NotNull PersonGender gender) {

  public UpdateUserPersonalDataCommand toCommand(UserId userId) {
    return new UpdateUserPersonalDataCommand(
        userId,
        PersonName.from(firstName, lastName),
        dateOfBirth,
        gender);
  }
}
