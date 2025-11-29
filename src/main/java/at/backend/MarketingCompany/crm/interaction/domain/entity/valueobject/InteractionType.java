package at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject;

public enum InteractionType {
    CALL("Phone conversation with customer"),
    MEETING("In-person or virtual meeting"),
    EMAIL("Email communication"),
    DEMONSTRATION("Product or service demonstration"),
    PROPOSAL("Business proposal presentation"),
    FOLLOW_UP("Follow-up communication"),
    SUPPORT("Customer support interaction"),
    FEEDBACK("Customer feedback collection"),
    NEGOTIATION("Business negotiation"),
    CONSULTATION("Professional consultation");

    private final String description;

    InteractionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCommunication() {
        return this == CALL || this == EMAIL || this == FOLLOW_UP;
    }

    public boolean isMeeting() {
        return this == MEETING || this == DEMONSTRATION || this == CONSULTATION;
    }

    public boolean isBusiness() {
        return this == PROPOSAL || this == NEGOTIATION;
    }
}
