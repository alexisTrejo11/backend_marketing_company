package at.backend.MarketingCompany.marketing.metric.core.domain.exception;

public class MetricBusinessRuleException extends CampaignMetricException {
	public MetricBusinessRuleException(String message) {
		super(message, "METRIC_BUSINESS_RULE_VIOLATION");
	}
}
