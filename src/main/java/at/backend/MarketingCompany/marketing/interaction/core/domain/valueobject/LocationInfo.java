package at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject;

import at.backend.MarketingCompany.marketing.interaction.core.domain.exception.InteractionValidationException;
import lombok.Getter;

@Getter
public class LocationInfo {

    private final String countryCode;
    private final String city;

    public LocationInfo() {
        this.countryCode = null;
        this.city = null;
    }

    private LocationInfo(String countryCode, String city) {
        this.countryCode = countryCode;
        this.city = city;
    }

    public static LocationInfo create(String countryCode, String city) {
        validateLocationInfo(countryCode, city);
        return new LocationInfo(
                sanitizeCountryCode(countryCode),
                sanitizeCity(city)
        );
    }

    public static LocationInfo reconstruct(String countryCode, String city) {
        return new LocationInfo(
                sanitizeCountryCode(countryCode),
                sanitizeCity(city)
        );
    }

    private static void validateLocationInfo(String countryCode, String city) {
        if (countryCode != null && !countryCode.matches("^[A-Z]{2}$")) {
            throw new InteractionValidationException("Country code must be 2 uppercase letters");
        }

        validateLength(city, "City", 100);
    }

    private static void validateLength(String value, String fieldName, int maxLength) {
        if (value != null && value.length() > maxLength) {
            throw new InteractionValidationException(
                    String.format("%s cannot exceed %d characters", fieldName, maxLength)
            );
        }
    }

    private static String sanitizeCountryCode(String countryCode) {
        return countryCode != null ? countryCode.trim().toUpperCase() : null;
    }

    private static String sanitizeCity(String city) {
        return city != null ? city.trim() : null;
    }

    public boolean hasLocationInfo() {
        return countryCode != null || city != null;
    }
}
