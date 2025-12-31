package at.backend.MarketingCompany.marketing.activity.core.domain.exception;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;

import java.util.Map;

public class ActivityStatusException extends CampaignActivityException {
	public ActivityStatusException(String message) {
		super(message, "CAMPAIGN_ACTIVITY_STATUS_ERROR");
	}
}
