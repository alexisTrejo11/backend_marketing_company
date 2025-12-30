package at.backend.MarketingCompany.marketing.metric.adapter.input.graphql.dto;


import at.backend.MarketingCompany.marketing.metric.core.application.command.UpdateMetricCommand;
import at.backend.MarketingCompany.marketing.metric.core.domain.valueobject.CampaignMetricId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateMetricInput(
		@NotNull @Positive Long metricId,
		String description,
		BigDecimal targetValue,
		String calculationFormula,
		String dataSource
) {
	public UpdateMetricCommand toCommand() {
		return new UpdateMetricCommand(
				metricId != null ? new CampaignMetricId(metricId) : null,
				description,
				targetValue,
				calculationFormula,
				dataSource
		);
	}
}
