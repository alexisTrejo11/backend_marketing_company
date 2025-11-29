package at.backend.MarketingCompany.account.auth.domain.entitiy.valueobject;

import at.backend.MarketingCompany.account.auth.domain.exceptions.AuthValidationException;

public record HashedPassword(String value) {
    public HashedPassword {
        if (value == null || value.isBlank()) {
            throw new AuthValidationException("Hashed password cannot be null or blank");
        }
        if (value.length() < 60) { // BCrypt hash length
            throw new AuthValidationException("Invalid hashed password format");
        }
    }

    public static HashedPassword from(String hashedPassword) {
        return hashedPassword != null ? new HashedPassword(hashedPassword) : null;
    }
}