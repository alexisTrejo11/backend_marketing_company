package at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject;

public enum OpportunityStage {
    LEAD,
    QUALIFIED,
    PROPOSAL,
    NEGOTIATION,
    CLOSED_WON,
    CLOSED_LOST;

    public boolean canTransitionTo(OpportunityStage newStage) {
        return switch (this) {
            case LEAD -> newStage == QUALIFIED || newStage == CLOSED_LOST;
            case QUALIFIED -> newStage == PROPOSAL || newStage == CLOSED_LOST;
            case PROPOSAL -> newStage == NEGOTIATION || newStage == CLOSED_LOST;
            case NEGOTIATION -> newStage == CLOSED_WON || newStage == CLOSED_LOST;
            case CLOSED_WON, CLOSED_LOST -> false;
        };
    }

    public boolean isActive() {
        return this != CLOSED_WON && this != CLOSED_LOST;
    }
}