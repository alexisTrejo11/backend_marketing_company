package at.backend.MarketingCompany.marketing.interaction.core.domain.exception;

import at.backend.MarketingCompany.shared.exception.DomainException;

public class CampaignInteractionDomainException extends DomainException {

    public CampaignInteractionDomainException(String message) {
        super(message, "CAMPAIGN_INTERACTION_DOMAIN_EXCEPTION");
    }

    public CampaignInteractionDomainException(String message, String code) {
        super(message, code);
    }
}
