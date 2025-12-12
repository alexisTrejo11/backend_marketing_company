package at.backend.MarketingCompany.account.auth.domain.entitiy.valueobject;

import at.backend.MarketingCompany.account.auth.domain.exceptions.AuthValidationException;

import java.util.regex.Pattern;

public record PlainPassword(String value) {
    private static final Pattern PASSWORD_PATTERN = 
        Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=.!,';])(?=\\S+$).{8,}$");

    public PlainPassword {
        if (value == null || value.isBlank()) {
            throw new AuthValidationException("PlainPassword cannot be null or blank");
        }
        if (value.length() < 8) {
            throw new AuthValidationException("PlainPassword must be at least 8 characters long");
        }
        if (value.length() > 128) {
            throw new AuthValidationException("PlainPassword cannot exceed 128 characters");
        }
        if (!PASSWORD_PATTERN.matcher(value).matches()) {
            throw new AuthValidationException(
                "PlainPassword must contain at least one digit, one lowercase, one uppercase, and one special character"
            );
        }
    }

    public static PlainPassword from(String password) {
        return password != null ? new PlainPassword(password) : null;
    }
}