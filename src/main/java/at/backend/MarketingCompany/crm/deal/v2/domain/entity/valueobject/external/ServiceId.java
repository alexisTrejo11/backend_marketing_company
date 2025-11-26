package at.backend.MarketingCompany.crm.deal.v2.domain.entity.valueobject.external;

import java.util.UUID;

public record ServiceId(UUID value) {
    public ServiceId {
        if (value == null) {
            throw new IllegalArgumentException("Deal ID cannot be null.");
        }
    }

    public static ServiceId create() {
        return new ServiceId(UUID.randomUUID());
    }
}