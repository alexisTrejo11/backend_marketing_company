package at.backend.MarketingCompany.crm.deal.v2.domain.entity.valueobject;

import java.util.UUID;

public record DealId(UUID value) {
    public DealId {
        if (value == null) {
            throw new IllegalArgumentException("Deal ID cannot be null.");
        }
    }

    public static DealId create() {
        return new DealId(UUID.randomUUID());
    }
}