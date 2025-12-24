package at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.dto;

import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.DealId;
import at.backend.MarketingCompany.marketing.interaction.core.application.command.MarkAsConversionCommand;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.CampaignInteractionId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record MarkConversionInput(
		@NotNull @Positive Long interactionId,
		@NotNull @Positive Long dealId,
		@NotNull @Positive Double conversionValue
) {

	public MarkAsConversionCommand toCommand() {
		return new MarkAsConversionCommand(
				new CampaignInteractionId(interactionId),
				new DealId(dealId),
				BigDecimal.valueOf(conversionValue)
		);
	}
}