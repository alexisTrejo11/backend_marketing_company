package at.backend.MarketingCompany.crm.interaction.application.commands;

import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.InteractionId;

public record MarkNegativeFeedbackCommand(InteractionId interactionId) {
    public static MarkNegativeFeedbackCommand from(String interactionId) {
        return new MarkNegativeFeedbackCommand(new InteractionId(interactionId));
    }
}
