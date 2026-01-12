package at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.marketing.interaction.core.application.command.UpdateInteractionPropertyCommand;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.CampaignInteractionId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdatePropertyInput(
    @NotNull Long interactionId,
    @NotBlank String key,
    Object value,
    String updatedBy,
    boolean overwrite) {

  public UpdateInteractionPropertyCommand toCommand() {
    return new UpdateInteractionPropertyCommand(
        new CampaignInteractionId(this.interactionId),
        this.key,
        this.value,
        this.updatedBy,
        this.overwrite);
  }
}
