package at.backend.MarketingCompany.crm.interaction.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.crm.interaction.core.application.commands.UpdateInteractionCommand;
import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record UpdateInteractionInput(
		@NotNull @Positive Long interactionId,
		@NotNull InteractionType type,
		@NotNull LocalDateTime dateTime,
		String description,
		@NotBlank String outcome) {

	public UpdateInteractionCommand toCommand() {
		return new UpdateInteractionCommand(
				new InteractionId(interactionId),
				type,
				InteractionDateTime.from(dateTime),
				InteractionDescription.from(description),
				InteractionOutcome.from(outcome));
	}
}
