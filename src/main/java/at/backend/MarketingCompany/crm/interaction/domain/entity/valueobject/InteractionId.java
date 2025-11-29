package at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject;

import java.util.UUID;

public record InteractionId(String value) {
    public InteractionId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Interaction ID cannot be null or blank");
        }
    }

    public static InteractionId create() {
        return new InteractionId(UUID.randomUUID().toString());
    }
    public static InteractionId from(String value) {
        return new InteractionId(value);
    }
}



