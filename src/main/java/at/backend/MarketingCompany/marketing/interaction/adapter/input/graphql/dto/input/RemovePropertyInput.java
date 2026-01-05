package at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.marketing.interaction.core.application.command.RemoveInteractionPropertyCommand;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.CampaignInteractionId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RemovePropertyInput(
    @NotNull Long interactionId,
    @NotBlank String key,
    String updatedBy) {

  public RemoveInteractionPropertyCommand toCommand() {
    return new RemoveInteractionPropertyCommand(
        new CampaignInteractionId(this.interactionId),
        this.key,
        this.updatedBy);
  }
}
