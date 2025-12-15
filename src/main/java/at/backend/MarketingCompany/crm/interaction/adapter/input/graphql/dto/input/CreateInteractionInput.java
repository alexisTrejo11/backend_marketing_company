package at.backend.MarketingCompany.crm.interaction.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.crm.interaction.core.application.commands.CreateInteractionCommand;
import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.ChannelPreference;
import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.FeedbackType;
import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.InteractionDateTime;
import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.InteractionDescription;
import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.InteractionOutcome;
import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.InteractionType;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateInteractionInput(
    @NotBlank String customerId,
    @NotNull InteractionType type,
    @NotNull LocalDateTime dateTime,
    String description,
    @NotBlank String outcome,
    FeedbackType feedbackType,
    String channelPreference) {

  public CreateInteractionCommand toCommand() {
    return new CreateInteractionCommand(
        new CustomerCompanyId(customerId()),
        type,
        InteractionDateTime.from(dateTime()),
        InteractionDescription.from(description()),
        InteractionOutcome.from(outcome()),
        feedbackType,
        ChannelPreference.from(channelPreference()));
  }
}
