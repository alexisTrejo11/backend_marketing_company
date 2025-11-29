package at.backend.MarketingCompany.crm.interaction.application.commands;

import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.FeedbackType;
import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.InteractionId;

public record AddFeedbackCommand(
    InteractionId interactionId,
    FeedbackType feedbackType,
    String notes
) {
    public static AddFeedbackCommand from(
            String interactionId,
            FeedbackType feedbackType,
            String notes
    ) {
        return new AddFeedbackCommand(
            new InteractionId(interactionId),
            feedbackType,
            notes
        );
    }
}
