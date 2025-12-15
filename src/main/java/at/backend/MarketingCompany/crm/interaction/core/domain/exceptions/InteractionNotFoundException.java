package at.backend.MarketingCompany.crm.interaction.core.domain.exceptions;

import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.InteractionId;

public class InteractionNotFoundException extends RuntimeException {
  public InteractionNotFoundException(InteractionId interactionId) {
    super("Interaction not found with ID: " + interactionId.value());
  }
}
