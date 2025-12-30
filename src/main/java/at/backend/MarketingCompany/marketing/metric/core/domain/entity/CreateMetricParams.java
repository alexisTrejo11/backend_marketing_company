package at.backend.MarketingCompany.marketing.metric.core.domain.entity;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MetricType;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CreateMetricParams(
		MarketingCampaignId campaignId,
		String name,
		MetricType metricType,
		String measurementUnit,
		String description,
		String calculationFormula,
		String dataSource,
		boolean isAutomated,
		BigDecimal targetValue
) {
}
