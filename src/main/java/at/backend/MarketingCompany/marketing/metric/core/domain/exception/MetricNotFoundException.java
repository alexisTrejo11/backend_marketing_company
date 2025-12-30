package at.backend.MarketingCompany.marketing.metric.core.domain.exception;

import at.backend.MarketingCompany.marketing.metric.core.domain.valueobject.CampaignMetricId;
import at.backend.MarketingCompany.shared.exception.NotFoundException;

public class MetricNotFoundException extends NotFoundException {
	public MetricNotFoundException(CampaignMetricId id) {
		super("Campaign Metric", id.getValue().toString());
	}
}
