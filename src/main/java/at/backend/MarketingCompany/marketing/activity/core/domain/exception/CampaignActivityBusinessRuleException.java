package at.backend.MarketingCompany.marketing.activity.core.domain.exception;

import at.backend.MarketingCompany.shared.exception.DomainException;

import java.util.Map;

public class CampaignActivityBusinessRuleException extends CampaignActivityException {
	public CampaignActivityBusinessRuleException(String message) {
		super(message, "CAMPAIGN_ACTIVITY_BUSINESS_RULE_VIOLATION");
	}

	public CampaignActivityBusinessRuleException(String message, String errorCode) {
		super(message, errorCode);
	}

	public CampaignActivityBusinessRuleException(String message, String errorCode, Map<String, Object> details) {
		super(message, errorCode, details);
	}

	public CampaignActivityBusinessRuleException(String message, String errorCode, Throwable cause) {
		super(message, errorCode, cause);
	}
}
