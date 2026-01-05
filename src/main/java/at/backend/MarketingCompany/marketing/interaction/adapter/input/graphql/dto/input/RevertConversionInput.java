package at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.marketing.interaction.core.application.command.RevertConversionCommand;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.CampaignInteractionId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RevertConversionInput(
    @NotNull @Positive Long interactionId,
    @NotBlank String reason,
    String revertedBy) {

  public RevertConversionCommand toCommand() {
    return new RevertConversionCommand(
        new CampaignInteractionId(interactionId),
        reason,
        revertedBy);
  }
}
