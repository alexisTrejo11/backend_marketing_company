package at.backend.MarketingCompany.marketing.activity.core.domain.exception;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.shared.exception.DomainException;

import java.util.Map;

public class CampaignActivityValidationException extends CampaignActivityException {
	public CampaignActivityValidationException(String message) {
		super(message, "CAMPAIGN_ACTIVITY_VALIDATION_ERROR");
	}

	public  CampaignActivityValidationException(MarketingCampaignId campaignId) {
		super("Validation failed for Campaign Activity in Campaign with ID: " + campaignId.asString(),
		      "CAMPAIGN_ACTIVITY_VALIDATION_ERROR");
	}

	public CampaignActivityValidationException(String message, String errorCode) {
		super(message, errorCode);
	}

	public CampaignActivityValidationException(String message, String errorCode, Map<String, Object> details) {
		super(message, errorCode, details);
	}

	public CampaignActivityValidationException(String message, String errorCode, Throwable cause) {
		super(message, errorCode, cause);
	}
}
