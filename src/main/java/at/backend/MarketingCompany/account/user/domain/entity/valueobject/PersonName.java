package at.backend.MarketingCompany.account.user.domain.entity.valueobject;

import at.backend.MarketingCompany.account.user.domain.exceptions.UserValidationException;

public record PersonName(String firstName, String lastName) {
    public PersonName {
        if (firstName == null || firstName.isBlank()) {
            throw new UserValidationException("First name is required");
        }
        if (firstName.length() > 50) {
            throw new UserValidationException("First name cannot exceed 50 characters");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new UserValidationException("Last name is required");
        }
        if (lastName.length() > 50) {
            throw new UserValidationException("Last name cannot exceed 50 characters");
        }
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public static PersonName from(String firstName, String lastName) {
        if (firstName == null || lastName == null) {
            return null;
        }
        return new PersonName(firstName.trim(), lastName.trim());
    }
}