package at.backend.MarketingCompany.marketing.metric.adapter.input.graphql.dto;

import at.backend.MarketingCompany.marketing.metric.core.domain.entity.CampaignMetric;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CampaignMetricOutput(
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
		String lastCalculatedDate,
		Boolean isAutomated,
		Boolean isTargetAchieved,
		BigDecimal achievementPercentage,
		String createdAt,
		String updatedAt
) {
}