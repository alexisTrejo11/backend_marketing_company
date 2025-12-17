package at.backend.MarketingCompany.crm.interaction.core.application.commands;

import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.InteractionId;

public record MarkPositiveFeedbackCommand(InteractionId interactionId) {
  public static MarkPositiveFeedbackCommand from(String interactionId) {
    return new MarkPositiveFeedbackCommand(InteractionId.of(interactionId));
  }
}
