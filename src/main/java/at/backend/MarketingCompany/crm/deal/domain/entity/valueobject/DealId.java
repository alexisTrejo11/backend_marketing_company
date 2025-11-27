package at.backend.MarketingCompany.crm.deal.domain.entity.valueobject;

import java.util.UUID;

public record DealId(String value) {
    public DealId {
        if (value == null) {
            throw new IllegalArgumentException("Deal ID cannot be null.");
        }
    }

    public String asString() {
        return value;
    }

    public static DealId from(String id) {
        return new DealId(id);
    }

    public static DealId create() {
        return new DealId(UUID.randomUUID().toString());
    }
}