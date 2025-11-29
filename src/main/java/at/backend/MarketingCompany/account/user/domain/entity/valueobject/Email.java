package at.backend.MarketingCompany.account.user.domain.entity.valueobject;

import at.backend.MarketingCompany.account.user.domain.exceptions.UserValidationException;

import java.util.regex.Pattern;

public record Email(String value) {
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public Email {
        if (value == null || value.isBlank()) {
            throw new UserValidationException("Email cannot be null or blank");
        }
        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new UserValidationException("Invalid email format: " + value);
        }
        if (value.length() > 255) {
            throw new UserValidationException("Email cannot exceed 255 characters");
        }
    }

    public String getDomain() {
        return value.substring(value.indexOf('@') + 1);
    }

    public static Email from(String email) {
        return email != null ? new Email(email.toLowerCase().trim()) : null;
    }
}