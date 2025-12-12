package at.backend.MarketingCompany.account.user.core.application.command;

import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.*;

import java.time.LocalDate;

public record UpdateUserPersonalDataCommand(
    UserId userId,
    PersonName name,
    LocalDate dateOfBirth,
    PersonGender gender
    ) {

    public PersonalData toPersonalData() {
        return new PersonalData(name, dateOfBirth, gender);
    }
}
