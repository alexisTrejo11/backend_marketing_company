package at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.dto.input;

import java.math.BigDecimal;

import at.backend.MarketingCompany.marketing.interaction.core.application.command.UpdateConversionValueCommand;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.CampaignInteractionId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateConversionValueInput(
    @NotNull Long interactionId,
    @NotNull @Positive Double newValue,
    @NotBlank String reason,
    String updatedBy) {

  public UpdateConversionValueCommand toCommand() {
    return new UpdateConversionValueCommand(
        new CampaignInteractionId(this.interactionId),
        BigDecimal.valueOf(this.newValue),
        this.reason,
        this.updatedBy);
  }
}
