package at.backend.MarketingCompany.marketing.metric.core.port.output;


import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MetricType;
import at.backend.MarketingCompany.marketing.metric.core.domain.entity.CampaignMetric;
import at.backend.MarketingCompany.marketing.metric.core.domain.valueobject.CampaignMetricId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public interface MetricRepositoryPort {

	CampaignMetric save(CampaignMetric metric);

	Optional<CampaignMetric> findById(CampaignMetricId id);

	void delete(CampaignMetricId id);

	Page<CampaignMetric> findByCampaignId(MarketingCampaignId campaignId, Pageable pageable);

	Page<CampaignMetric> findByCampaignIdAndMetricType(MarketingCampaignId campaignId, MetricType metricType, Pageable pageable);

	Page<CampaignMetric> findByAutomationStatus(Boolean isAutomated, Pageable pageable);

	Page<CampaignMetric> findRecentlyUpdated(LocalDateTime fromDate, Pageable pageable);

	Page<CampaignMetric> findByCampaignIdAndTargetAchievement(MarketingCampaignId campaignId, Boolean isAchieved, Pageable pageable);

	long countAchievedMetricsByCampaignId(MarketingCampaignId campaignId);

	BigDecimal calculateAverageValueByCampaignAndMetricType(MarketingCampaignId campaignId, MetricType metricType);

	boolean existsByCampaignIdAndNameAndNotDeleted(MarketingCampaignId campaignId, String name);
}