package at.backend.MarketingCompany.crm.interaction.core.application.commands;

import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.InteractionId;

public record MarkNegativeFeedbackCommand(InteractionId interactionId) {
  public static MarkNegativeFeedbackCommand from(String interactionId) {
    return new MarkNegativeFeedbackCommand(new InteractionId(interactionId));
  }
}
