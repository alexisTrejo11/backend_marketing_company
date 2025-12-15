package at.backend.MarketingCompany.crm.interaction.core.application.commands;

import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.InteractionId;

public record MarkNeutralFeedbackCommand(InteractionId interactionId) {
  public static MarkNeutralFeedbackCommand from(String interactionId) {
    return new MarkNeutralFeedbackCommand(new InteractionId(interactionId));
  }
}
