package at.backend.MarketingCompany.marketing.metric.core.port.input;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.metric.core.application.dto.MetricPerformanceSummary;
import at.backend.MarketingCompany.marketing.metric.core.application.dto.MetricStatistics;
import at.backend.MarketingCompany.marketing.metric.core.domain.valueobject.CampaignMetricId;

public interface MetricStatisticInputPort {
	MetricStatistics getMetricStatistics(MarketingCampaignId campaignId);

	MetricPerformanceSummary getMetricPerformance(CampaignMetricId metricId);

	Double getAchievementRate(MarketingCampaignId campaignId);
}