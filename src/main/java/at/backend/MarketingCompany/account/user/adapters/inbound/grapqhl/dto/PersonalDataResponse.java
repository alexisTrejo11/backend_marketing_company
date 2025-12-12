package at.backend.MarketingCompany.account.user.adapters.inbound.grapqhl.dto;

import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.PersonGender;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.PersonalData;

import java.time.LocalDate;

public record PersonalDataResponse(
        String firstName,
        String lastName,
        PersonGender gender,
        LocalDate dateOfBirth
) {
    public PersonalDataResponse fromDomain(PersonalData personalData) {
        if (personalData == null) {
            return null;
        }

        String firstName = null;
        String lastName = null;

        if (personalData.name() != null) {
            firstName = personalData.name().firstName();
            lastName = personalData.name().lastName();
        }

        return new PersonalDataResponse(
                firstName,
                lastName,
                personalData.gender(),
                personalData.dateOfBirth()
        );

    }
}
