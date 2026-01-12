package at.backend.MarketingCompany.marketing.metric.adapter.input.graphql.dto;

import at.backend.MarketingCompany.marketing.metric.core.application.command.UpdateMetricValueCommand;
import at.backend.MarketingCompany.marketing.metric.core.domain.valueobject.CampaignMetricId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateMetricValueInput(
		@NotNull @Positive Long metricId,
		@NotNull BigDecimal newValue
) {
	public UpdateMetricValueCommand toCommand() {
		return new UpdateMetricValueCommand(
				metricId != null ? new CampaignMetricId(metricId) : null,
				newValue
		);
	}
}
