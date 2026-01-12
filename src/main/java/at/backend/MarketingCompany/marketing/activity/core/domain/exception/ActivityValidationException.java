package at.backend.MarketingCompany.marketing.activity.core.domain.exception;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;

import java.util.Map;

public class ActivityValidationException extends CampaignActivityException {
	public ActivityValidationException(String message) {
		super(message, "CAMPAIGN_ACTIVITY_VALIDATION_ERROR");
	}

	public ActivityValidationException(String message, String code) {
		super(message, code);
	}


	public ActivityValidationException(MarketingCampaignId campaignId) {
		super("Validation failed for Campaign Activity in Campaign with ID: " + campaignId.asString(),
		      "CAMPAIGN_ACTIVITY_VALIDATION_ERROR");
	}


}
