package at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.marketing.interaction.core.application.command.UpdateDeviceInfoCommand;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.CampaignInteractionId;
import jakarta.validation.constraints.NotNull;

public record UpdateDeviceInfoInput(
    @NotNull Long interactionId,
    String deviceType,
    String deviceOs,
    String browser,
    String updatedBy) {

  public UpdateDeviceInfoCommand toCommand() {
    return new UpdateDeviceInfoCommand(
        new CampaignInteractionId(this.interactionId),
        this.deviceType,
        this.deviceOs,
        this.browser,
        this.updatedBy);
  }
}
