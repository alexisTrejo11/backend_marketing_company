package at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.marketing.interaction.core.application.command.UpdateUTMParametersCommand;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.CampaignInteractionId;
import jakarta.validation.constraints.NotNull;

public record UpdateUTMParametersInput(
    @NotNull Long interactionId,
    String source,
    String medium,
    String campaign,
    String content,
    String term,
    String updatedBy) {

  public UpdateUTMParametersCommand toCommand() {
    return new UpdateUTMParametersCommand(
        new CampaignInteractionId(this.interactionId),
        this.source,
        this.medium,
        this.campaign,
        this.content,
        this.term,
        this.updatedBy);
  }

}
