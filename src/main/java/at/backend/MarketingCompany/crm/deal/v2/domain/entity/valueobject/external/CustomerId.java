package at.backend.MarketingCompany.crm.deal.v2.domain.entity.valueobject.external;

import java.util.UUID;

public record CustomerId(UUID value) {
    public CustomerId {
        if (value == null) {
            throw new IllegalArgumentException("Deal ID cannot be null.");
        }
    }

    public static CustomerId create() {
        return new CustomerId(UUID.randomUUID());
    }
}