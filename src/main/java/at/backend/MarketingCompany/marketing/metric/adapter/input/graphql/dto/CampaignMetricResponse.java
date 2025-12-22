package at.backend.MarketingCompany.marketing.metric.adapter.input.graphql.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CampaignMetricResponse(
    Long id,
    Long campaignId,
    String name,
    String metricType,
    String description,
    BigDecimal currentValue,
    BigDecimal targetValue,
    String measurementUnit,
    String calculationFormula,
    String dataSource,
    LocalDateTime lastCalculatedDate,
    Boolean isAutomated,
    Boolean isTargetAchieved,
    BigDecimal achievementPercentage,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}