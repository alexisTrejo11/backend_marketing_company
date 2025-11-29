package at.backend.MarketingCompany.crm.interaction.application.commands;

import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.InteractionId;

public record DeleteInteractionCommand(InteractionId interactionId) {
    public static DeleteInteractionCommand from(String interactionId) {
        return new DeleteInteractionCommand(new InteractionId(interactionId));
    }
}
