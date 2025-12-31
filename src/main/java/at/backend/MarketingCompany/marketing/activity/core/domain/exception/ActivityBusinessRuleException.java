package at.backend.MarketingCompany.marketing.activity.core.domain.exception;

import java.util.Map;

public class ActivityBusinessRuleException extends CampaignActivityException {
	public ActivityBusinessRuleException(String message) {
		super(message, "CAMPAIGN_ACTIVITY_BUSINESS_RULE_VIOLATION");
	}

	public ActivityBusinessRuleException(String message, String errorCode) {
		super(message, errorCode);
	}

	public ActivityBusinessRuleException(String message, String errorCode, Map<String, Object> details) {
		super(message, errorCode, details);
	}

	public ActivityBusinessRuleException(String message, String errorCode, Throwable cause) {
		super(message, errorCode, cause);
	}
}
