package at.backend.MarketingCompany.account.user.core.domain.entity.valueobject;

import java.util.regex.Pattern;

import at.backend.MarketingCompany.account.user.core.domain.exceptions.UserValidationException;

public record PhoneNumber(String value) {
  private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[1-9]\\d{1,14}$"); // E.164 format

  public void validate() {
    if (value == null || value.isBlank()) {
      throw new UserValidationException("Phone number cannot be null or blank");
    }
    if (!PHONE_PATTERN.matcher(value).matches()) {
      throw new UserValidationException("Invalid phone number format: " + value);
    }
  }

  public static PhoneNumber create(String phoneNumber) {
    PhoneNumber phone = new PhoneNumber(phoneNumber);
    phone.validate();
    return phone;
  }

  public static PhoneNumber from(String phoneNumber) {
    return phoneNumber != null ? new PhoneNumber(phoneNumber) : null;
  }
}
