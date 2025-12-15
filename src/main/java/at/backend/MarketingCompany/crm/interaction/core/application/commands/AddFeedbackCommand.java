package at.backend.MarketingCompany.crm.interaction.core.application.commands;

import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.InteractionId;
import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.FeedbackType;

public record AddFeedbackCommand(InteractionId interactionId, FeedbackType feedbackType, String notes) {

  public static AddFeedbackCommand from(String interactionId, FeedbackType feedbackType, String notes) {
    return new AddFeedbackCommand(
        new InteractionId(interactionId),
        feedbackType,
        notes);
  }
}
