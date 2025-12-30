package at.backend.MarketingCompany.marketing.metric.core.application.command;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MetricType;
import at.backend.MarketingCompany.marketing.metric.core.domain.entity.CreateMetricParams;

import java.math.BigDecimal;

public record CreateMetricCommand(
    MarketingCampaignId campaignId,
    String name,
    MetricType metricType,
    String description,
    BigDecimal targetValue,
    String measurementUnit,
    String calculationFormula,
    String dataSource,
    Boolean isAutomated
) {
	public CreateMetricParams toCreateParams() {
		return CreateMetricParams.builder()
				.campaignId(campaignId)
				.name(name)
				.metricType(metricType)
				.description(description)
				.targetValue(targetValue)
				.measurementUnit(measurementUnit)
				.calculationFormula(calculationFormula)
				.dataSource(dataSource)
				.isAutomated(isAutomated != null ? isAutomated : false)
				.build();
	}
}