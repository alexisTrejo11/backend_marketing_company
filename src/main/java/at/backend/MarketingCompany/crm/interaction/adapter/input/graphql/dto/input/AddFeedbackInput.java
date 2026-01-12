package at.backend.MarketingCompany.crm.interaction.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.crm.interaction.core.application.commands.AddFeedbackCommand;
import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.FeedbackType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddFeedbackInput(@NotBlank String interactionId, @NotNull FeedbackType feedbackType, String notes) {

  public AddFeedbackCommand toCommand() {
    return AddFeedbackCommand.from(
        interactionId(),
        feedbackType(),
        notes());
  }
}
