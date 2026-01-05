package at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject;

import at.backend.MarketingCompany.marketing.interaction.core.domain.exception.InteractionValidationException;
import lombok.Getter;

@Getter
public class DeviceInfo {

    private final String type;
    private final String os;
    private final String browser;

    public DeviceInfo() {
        this.type = null;
        this.os = null;
        this.browser = null;
    }

    private DeviceInfo(String type, String os, String browser) {
        this.type = type;
        this.os = os;
        this.browser = browser;
    }

    public static DeviceInfo create(String type, String os, String browser) {
        validateDeviceInfo(type, os, browser);
        return new DeviceInfo(
                sanitize(type),
                sanitize(os),
                sanitize(browser)
        );
    }

    public static DeviceInfo reconstruct(String type, String os, String browser) {
        validateDeviceInfo(type, os, browser);
        return new DeviceInfo(
                sanitize(type),
                sanitize(os),
                sanitize(browser)
        );
    }

    private static void validateDeviceInfo(String type, String os, String browser) {
        validateLength(type, "Device type", 50);
        validateLength(os, "Device OS", 50);
        validateLength(browser, "Browser", 100);
    }

    private static void validateLength(String value, String fieldName, int maxLength) {
        if (value != null && value.length() > maxLength) {
            throw new InteractionValidationException(
                    String.format("%s cannot exceed %d characters", fieldName, maxLength)
            );
        }
    }

    private static String sanitize(String value) {
        return value != null ? value.trim() : null;
    }

    public boolean hasDeviceInfo() {
        return type != null || os != null || browser != null;
    }
}
