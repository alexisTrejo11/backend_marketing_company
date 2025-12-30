package at.backend.MarketingCompany.marketing.metric.core.port.input;

import at.backend.MarketingCompany.marketing.metric.core.application.command.CreateMetricCommand;
import at.backend.MarketingCompany.marketing.metric.core.application.command.UpdateMetricCommand;
import at.backend.MarketingCompany.marketing.metric.core.application.command.UpdateMetricValueCommand;
import at.backend.MarketingCompany.marketing.metric.core.domain.entity.CampaignMetric;
import at.backend.MarketingCompany.marketing.metric.core.domain.valueobject.CampaignMetricId;

public interface MetricCommandInputPort {

	CampaignMetric createMetric(CreateMetricCommand command);

	CampaignMetric updateMetric(UpdateMetricCommand command);

	CampaignMetric updateMetricValue(UpdateMetricValueCommand command);

	CampaignMetric markAsAutomated(CampaignMetricId metricId);

	void deleteMetric(CampaignMetricId metricId);
}