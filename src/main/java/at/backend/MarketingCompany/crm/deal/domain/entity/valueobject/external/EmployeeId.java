package at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external;

import java.util.UUID;

public record EmployeeId(String value) {
    public EmployeeId {
        if (value == null) {
            throw new IllegalArgumentException("Deal ID cannot be null.");
        }
    }

    public static EmployeeId generate() {
        return new EmployeeId(UUID.randomUUID().toString());
    }
}