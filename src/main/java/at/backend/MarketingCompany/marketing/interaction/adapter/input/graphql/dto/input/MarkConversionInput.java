package at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.dto.input;

import java.math.BigDecimal;

import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.DealId;
import at.backend.MarketingCompany.marketing.interaction.core.application.command.MarkAsConversionCommand;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.CampaignInteractionId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MarkConversionInput(
    @NotNull @Positive Long interactionId,
    @NotNull @Positive Long dealId,
    @NotNull @Positive BigDecimal conversionValue,
    String notes) {

  public MarkAsConversionCommand toCommand() {
    return new MarkAsConversionCommand(
        new CampaignInteractionId(interactionId),
        new DealId(dealId),
        conversionValue,
        notes);
  }
}
