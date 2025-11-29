package at.backend.MarketingCompany.crm.interaction.application.commands;

import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.InteractionId;

public record MarkPositiveFeedbackCommand(InteractionId interactionId) {
    public static MarkPositiveFeedbackCommand from(String interactionId) {
        return new MarkPositiveFeedbackCommand(new InteractionId(interactionId));
    }
}
