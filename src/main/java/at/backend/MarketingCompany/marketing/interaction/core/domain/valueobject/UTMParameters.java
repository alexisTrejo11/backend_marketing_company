package at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject;

import at.backend.MarketingCompany.marketing.interaction.core.domain.exception.InteractionValidationException;
import lombok.Getter;

@Getter
public class UTMParameters {

    private final String source;
    private final String medium;
    private final String campaign;
    private final String content;
    private final String term;

    public UTMParameters() {
        this.source = null;
        this.medium = null;
        this.campaign = null;
        this.content = null;
        this.term = null;
    }

    private UTMParameters(String source, String medium, String campaign,
            String content, String term) {
        this.source = source;
        this.medium = medium;
        this.campaign = campaign;
        this.content = content;
        this.term = term;
    }

    public static UTMParameters reconstruct(String source, String medium, String campaign, String content, String term) {
        return new UTMParameters(
                source,
                medium,
                campaign,
                content,
                term
        );
    }

    public static UTMParameters create(String source, String medium, String campaign,
            String content, String term) {
        validateUTMParameters(source, medium, campaign, content, term);
        return new UTMParameters(
                (source),
                sanitize(medium),
                sanitize(campaign),
                sanitize(content),
                sanitize(term)
        );
    }

    private static void validateUTMParameters(String source, String medium, String campaign,
            String content, String term) {
        validateLength(source, "UTM Source", 100);
        validateLength(medium, "UTM Medium", 100);
        validateLength(campaign, "UTM Campaign", 100);
        validateLength(content, "UTM Content", 100);
        validateLength(term, "UTM Term", 100);

        validateCharacters(source, "UTM Source");
        validateCharacters(medium, "UTM Medium");
        validateCharacters(campaign, "UTM Campaign");
        validateCharacters(content, "UTM Content");
        validateCharacters(term, "UTM Term");
    }

    private static void validateLength(String value, String fieldName, int maxLength) {
        if (value != null && value.length() > maxLength) {
            throw new InteractionValidationException(
                    String.format("%s cannot exceed %d characters", fieldName, maxLength)
            );
        }
    }

    private static void validateCharacters(String value, String fieldName) {
        if (value != null && !value.matches("^[a-zA-Z0-9\\s\\-_.]+$")) {
            throw new InteractionValidationException(
                    String.format("%s contains invalid characters", fieldName)
            );
        }
    }

    private static String sanitize(String value) {
        return value != null ? value.trim() : null;
    }

    public boolean isEmpty() {
        return source == null && medium == null && campaign == null
                && content == null && term == null;
    }
}
