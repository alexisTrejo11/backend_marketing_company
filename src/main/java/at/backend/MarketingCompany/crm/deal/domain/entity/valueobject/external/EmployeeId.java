package at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external;

import java.util.UUID;

public record EmployeeId(UUID value) {
    public EmployeeId {
        if (value == null) {
            throw new IllegalArgumentException("Deal ID cannot be null.");
        }
    }

    public static EmployeeId create() {
        return new EmployeeId(UUID.randomUUID());
    }
}