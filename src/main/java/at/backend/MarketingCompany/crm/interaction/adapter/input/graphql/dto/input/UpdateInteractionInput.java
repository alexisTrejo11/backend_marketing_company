package at.backend.MarketingCompany.crm.interaction.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.crm.interaction.core.application.commands.UpdateInteractionCommand;
import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.InteractionDateTime;
import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.InteractionDescription;
import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.InteractionOutcome;
import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.InteractionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record UpdateInteractionInput(
    @NotBlank String interactionId,
    @NotNull InteractionType type,
    @NotNull LocalDateTime dateTime,
    String description,
    @NotBlank String outcome) {

  public UpdateInteractionCommand toCommand() {
    return new UpdateInteractionCommand(
        interactionId,
        type,
        InteractionDateTime.from(dateTime),
        InteractionDescription.from(description),
        InteractionOutcome.from(outcome));
  }
}
