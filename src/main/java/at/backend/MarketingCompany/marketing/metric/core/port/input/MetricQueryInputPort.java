package at.backend.MarketingCompany.marketing.metric.core.port.input;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.metric.core.application.query.MetricQuery;
import at.backend.MarketingCompany.marketing.metric.core.domain.entity.CampaignMetric;
import at.backend.MarketingCompany.marketing.metric.core.domain.valueobject.CampaignMetricId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface MetricQueryInputPort {
	CampaignMetric getMetricById(CampaignMetricId metricId);

	Page<CampaignMetric> searchMetrics(MetricQuery query, Pageable pageable);

	Page<CampaignMetric> getMetricsByCampaign(MarketingCampaignId campaignId, Pageable pageable);

	Page<CampaignMetric> getAchievedMetrics(MarketingCampaignId campaignId, Pageable pageable);

	Page<CampaignMetric> getNotAchievedMetrics(MarketingCampaignId campaignId, Pageable pageable);

	Page<CampaignMetric> getAutomatedMetrics(MarketingCampaignId campaignId, Pageable pageable);

	Page<CampaignMetric> getRecentlyUpdatedMetrics(LocalDateTime fromDate, Pageable pageable);
}