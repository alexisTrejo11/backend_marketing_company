package at.backend.MarketingCompany.account.user.core.domain.entity.valueobject;

import at.backend.MarketingCompany.account.user.core.domain.exceptions.UserValidationException;

import java.util.regex.Pattern;

public record PhoneNumber(String value) {
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^\\+?[1-9]\\d{1,14}$"); // E.164 format

    public PhoneNumber {
        if (value != null) {
            String cleaned = value.replaceAll("\\s+", "");
            if (!PHONE_PATTERN.matcher(cleaned).matches()) {
                throw new UserValidationException("Invalid phone number format: " + value);
            }
        }
    }

    public static PhoneNumber from(String phoneNumber) {
        return phoneNumber != null ? new PhoneNumber(phoneNumber) : null;
    }
}
