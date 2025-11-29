package at.backend.MarketingCompany.crm.interaction.application.commands;

import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.InteractionId;

public record MarkNeutralFeedbackCommand(InteractionId interactionId) {
    public static MarkNeutralFeedbackCommand from(String interactionId) {
        return new MarkNeutralFeedbackCommand(new InteractionId(interactionId));
    }
}
