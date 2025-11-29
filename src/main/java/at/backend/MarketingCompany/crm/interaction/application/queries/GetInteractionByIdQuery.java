package at.backend.MarketingCompany.crm.interaction.application.queries;


import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.InteractionId;

public record GetInteractionByIdQuery(InteractionId interactionId) {
    public static GetInteractionByIdQuery from(String id) {
        return new GetInteractionByIdQuery(new InteractionId(id));
    }
}

