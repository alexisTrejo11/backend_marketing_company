package at.backend.MarketingCompany.account.user.core.domain.entity.valueobject;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public record PersonalData(PersonName name, LocalDate dateOfBirth, PersonGender gender) {
    public PersonalData {
        validate(name, dateOfBirth, gender);
        if (gender == null) {
            gender = PersonGender.UNKNOWN;
        }
    }

    public static void validate(PersonName name, LocalDate dateOfBirth, PersonGender gender) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }

        if (dateOfBirth == null) {
            throw new IllegalArgumentException("Date of birth cannot be null");
        }

        if (dateOfBirth.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth cannot be in the future");
        }

        if (LocalDate.now().minusYears(16).isBefore(dateOfBirth)) {
            throw new IllegalArgumentException("User must be at least 16 years old");
        }

        if (gender == null) {
            log.warn("Gender is null, setting to UNKNOWN");
        }
    }
}
