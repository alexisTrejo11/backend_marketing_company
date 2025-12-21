package at.backend.MarketingCompany.marketing.metric.core.domain.entity;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MetricType;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.metric.core.domain.valueobject.CampaignMetricId;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record CampaignMetricReconstructParams(
    CampaignMetricId id,
    MarketingCampaignId campaignId,
    String name,
    MetricType metricType,
    String description,
    BigDecimal currentValue,
    BigDecimal targetValue,
    String measurementUnit,
    String calculationFormula,
    String dataSource,
    LocalDateTime lastCalculatedDate,
    Boolean isAutomated,
    Boolean isTargetAchieved,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime deletedAt,
    Integer version
) {}