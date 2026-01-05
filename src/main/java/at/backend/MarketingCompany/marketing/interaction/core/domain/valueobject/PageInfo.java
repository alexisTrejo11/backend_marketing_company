package at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject;

import java.net.MalformedURLException;
import java.net.URI;

import at.backend.MarketingCompany.marketing.interaction.core.domain.exception.InteractionValidationException;
import lombok.Getter;

@Getter
public class PageInfo {

    private final String landingPageUrl;
    private final String referrerUrl;

    public PageInfo() {
        this.landingPageUrl = null;
        this.referrerUrl = null;
    }

    private PageInfo(String landingPageUrl, String referrerUrl) {
        this.landingPageUrl = landingPageUrl;
        this.referrerUrl = referrerUrl;
    }

    public static PageInfo create(String landingPageUrl, String referrerUrl) {
        validateUrls(landingPageUrl, referrerUrl);
        return new PageInfo(
                sanitizeUrl(landingPageUrl),
                sanitizeUrl(referrerUrl)
        );
    }

    public static PageInfo reconstruct(String landingPageUrl, String referrerUrl) {
        return new PageInfo(
                sanitizeUrl(landingPageUrl),
                sanitizeUrl(referrerUrl)
        );
    }

    private static void validateUrls(String landingPageUrl, String referrerUrl) {
        validateUrl(landingPageUrl, "Landing page URL", 2000);
        validateUrl(referrerUrl, "Referrer URL", 2000);
    }

    private static void validateUrl(String url, String fieldName, int maxLength) {
        if (url == null || url.isBlank()) {
            return;
        }

        if (url.length() > maxLength) {
            throw new InteractionValidationException(
                    String.format("%s cannot exceed %d characters", fieldName, maxLength)
            );
        }

        try {
            URI.create(url).toURL();
        } catch (MalformedURLException | IllegalArgumentException e) {
            throw new InteractionValidationException(
                    String.format("%s is not a valid URL", fieldName)
            );
        }
    }

    private static String sanitizeUrl(String url) {
        return url != null ? url.trim() : null;
    }

    public boolean hasLandingPage() {
        return landingPageUrl != null && !landingPageUrl.isBlank();
    }

    public boolean hasReferrer() {
        return referrerUrl != null && !referrerUrl.isBlank();
    }
}
