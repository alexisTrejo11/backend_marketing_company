package at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external;

import java.util.UUID;

public record ServiceId(String value) {
    public ServiceId {
        if (value == null) {
            throw new IllegalArgumentException("Deal ID cannot be null.");
        }
    }

    public String asString() {
        return value;
    }

    public static ServiceId from(UUID id) {
        return new ServiceId(id.toString());
    }

    public static ServiceId create() {
        return new ServiceId(UUID.randomUUID().toString());
    }
}