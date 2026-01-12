package at.backend.MarketingCompany.marketing.interaction.core.domain.exception;

public class InteractionValidationException extends CampaignInteractionDomainException {

    public InteractionValidationException(String message) {
        super(message, "CAMPAIGN_INTERACTION_VALIDATION_DOMAIN_EXCEPTION");
    }

    public InteractionValidationException(String message, String code) {
        super(message, code);
    }
}
