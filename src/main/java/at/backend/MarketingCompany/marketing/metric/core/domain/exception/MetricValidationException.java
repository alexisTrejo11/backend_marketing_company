package at.backend.MarketingCompany.marketing.metric.core.domain.exception;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;

public class MetricValidationException extends CampaignMetricException {
	public MetricValidationException(String message) {
		super(message, "METRIC_VALIDATION_ERROR");
	}

	public MetricValidationException(MarketingCampaignId id) {
		super("Invalid Campaign ID provided: " + id.getValue(), "METRIC_VALIDATION_ERROR");
	}
}
