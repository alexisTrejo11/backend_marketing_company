package at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.marketing.interaction.core.application.command.UpdateLocationInfoCommand;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.CampaignInteractionId;
import jakarta.validation.constraints.NotNull;

public record UpdateLocationInfoInput(
    @NotNull Long interactionId,
    String countryCode,
    String city,
    String updatedBy) {

  public UpdateLocationInfoCommand toCommand() {
    return new UpdateLocationInfoCommand(
        new CampaignInteractionId(this.interactionId),
        this.countryCode,
        this.city,
        this.updatedBy);
  }

}
