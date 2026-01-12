package at.backend.MarketingCompany.crm.interaction.core.application.queries;

import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.InteractionId;

public record GetInteractionByIdQuery(InteractionId interactionId) {
  public static GetInteractionByIdQuery from(String id) {
    return new GetInteractionByIdQuery(InteractionId.of(id));
  }
}
