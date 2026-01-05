package at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.marketing.interaction.core.application.command.UpdatePageInfoCommand;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.CampaignInteractionId;
import jakarta.validation.constraints.NotNull;

public record UpdatePageInfoInput(
    @NotNull Long interactionId,
    String landingPageUrl,
    String referrerUrl,
    String updatedBy) {

  public UpdatePageInfoCommand toCommand() {
    return new UpdatePageInfoCommand(
        new CampaignInteractionId(this.interactionId),
        this.landingPageUrl,
        this.referrerUrl,
        this.updatedBy);
  }
}
