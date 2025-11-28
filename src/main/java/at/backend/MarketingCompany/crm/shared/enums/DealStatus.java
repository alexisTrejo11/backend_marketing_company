package at.backend.MarketingCompany.crm.shared.enums;

public enum DealStatus {
    DRAFT,
    IN_NEGOTIATION,
    SIGNED,
    PAID,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED;

    public boolean canTransitionTo(DealStatus newStatus) {
        return switch (this) {
            case DRAFT -> newStatus == IN_NEGOTIATION || newStatus == CANCELLED || newStatus == SIGNED;
            case IN_NEGOTIATION -> newStatus == SIGNED || newStatus == CANCELLED;
            case SIGNED -> newStatus == PAID || newStatus == CANCELLED;
            case PAID -> newStatus == IN_PROGRESS || newStatus == CANCELLED;
            case IN_PROGRESS -> newStatus == COMPLETED || newStatus == CANCELLED;
            case COMPLETED, CANCELLED -> false;
        };
    }
    }