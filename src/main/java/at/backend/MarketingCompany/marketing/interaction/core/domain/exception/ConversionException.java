package at.backend.MarketingCompany.marketing.interaction.core.domain.exception;

public class ConversionException extends CampaignInteractionDomainException {

    public ConversionException(String message) {
        super(message, extracted());
    }

    private static String extracted() {
        return "CAMPAIGN_INTERACTION_CONVERSION_EXCEPTION";
    }

    public ConversionException(String message, String code) {
        super(message, code);
    }
}
