package at.backend.MarketingCompany.account.auth.core.application.commands;

import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.*;

import java.time.LocalDate;

public record UpdateProfileCommand(
    UserId userId,
    PersonName name,
    LocalDate dateOfBirth,
    PersonGender gender
    ) {

    public PersonalData toPersonalData() {
        return new PersonalData(name, dateOfBirth, gender);
    }
}
