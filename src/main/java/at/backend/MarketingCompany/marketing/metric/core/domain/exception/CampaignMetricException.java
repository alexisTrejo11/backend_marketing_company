package at.backend.MarketingCompany.marketing.metric.core.domain.exception;

import at.backend.MarketingCompany.shared.exception.DomainException;

import java.util.Map;

public class CampaignMetricException extends DomainException {
	public CampaignMetricException(String message) {
		super(message, "CAMPAIGN_METRIC_ERROR");
	}

	public CampaignMetricException(String message, String errorCode) {
		super(message, errorCode);
	}

	public CampaignMetricException(String message, String errorCode, Map<String, Object> details) {
		super(message, errorCode, details);
	}

	public CampaignMetricException(String message, String errorCode, Throwable cause) {
		super(message, errorCode, cause);
	}
}
