package at.backend.MarketingCompany.account.auth.core.domain.entitiy.valueobject;

import at.backend.MarketingCompany.account.auth.core.domain.exceptions.AuthValidationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public record HashedPassword(String value) {
    public HashedPassword {
        if (value == null || value.isBlank()) {
            throw new AuthValidationException("Hashed password cannot be null or blank");
        }
        if (value.length() < 60) { // BCrypt hash length
					log.warn("Hashed password length is less than expected: {}", value.length());
					//throw new AuthValidationException("Invalid hashed password format");
        }
    }

    public static HashedPassword from(String hashedPassword) {
        return hashedPassword != null ? new HashedPassword(hashedPassword) : null;
    }
}