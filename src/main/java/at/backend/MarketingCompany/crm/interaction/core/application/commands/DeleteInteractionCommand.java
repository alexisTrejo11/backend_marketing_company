package at.backend.MarketingCompany.crm.interaction.core.application.commands;

import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.InteractionId;

public record DeleteInteractionCommand(InteractionId interactionId) {
  public static DeleteInteractionCommand from(String interactionId) {
    return new DeleteInteractionCommand(new InteractionId(interactionId));
  }
}
