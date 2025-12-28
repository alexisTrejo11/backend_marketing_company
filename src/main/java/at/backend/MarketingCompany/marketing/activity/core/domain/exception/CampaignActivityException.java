package at.backend.MarketingCompany.marketing.activity.core.domain.exception;

import at.backend.MarketingCompany.shared.exception.DomainException;

import java.util.Map;

public class CampaignActivityException extends DomainException {
	public CampaignActivityException(String message) {
		super(message, "CAMPAIGN_ACTIVITY_ERROR");
	}

	public CampaignActivityException(String message, String errorCode) {
		super(message, errorCode);
	}

	public CampaignActivityException(String message, String errorCode, Map<String, Object> details) {
		super(message, errorCode, details);
	}

	public CampaignActivityException(String message, String errorCode, Throwable cause) {
		super(message, errorCode, cause);
	}
}
