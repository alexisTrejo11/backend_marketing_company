package at.backend.MarketingCompany.crm.interaction.core.application.commands;

import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.*;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;

public record CreateInteractionCommand(
    CustomerCompanyId customerCompanyId,
    InteractionType type,
    InteractionDateTime dateTime,
    InteractionDescription description,
    InteractionOutcome outcome,
    FeedbackType feedbackType,
    ChannelPreference channelPreference) {

  public CreateInteractionParams toCreateParams() {
    return CreateInteractionParams.builder()
        .customerCompanyId(customerCompanyId)
        .type(type)
        .dateTime(dateTime)
        .description(description)
        .outcome(outcome)
        .feedbackType(feedbackType)
        .channelPreference(channelPreference)
        .build();
  }
}
