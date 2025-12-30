package at.backend.MarketingCompany.marketing.metric.adapter.input.graphql.dto;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MetricType;
import at.backend.MarketingCompany.marketing.metric.core.application.command.CreateMetricCommand;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateMetricInput(
		@NotNull @Positive Long campaignId,
		String name,
		MetricType metricType,
		String description,
		BigDecimal targetValue,
		String measurementUnit,
		String calculationFormula,
		String dataSource,
		Boolean isAutomated
) {
	
	public CreateMetricCommand toCommand() {
		return new CreateMetricCommand(
				campaignId != null ? new MarketingCampaignId(campaignId) : null,
				name,
				metricType,
				description,
				targetValue,
				measurementUnit,
				calculationFormula,
				dataSource,
				isAutomated
		);
	}
}