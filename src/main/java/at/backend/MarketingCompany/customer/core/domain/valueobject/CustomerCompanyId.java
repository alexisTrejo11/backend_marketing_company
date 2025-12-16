package at.backend.MarketingCompany.customer.core.domain.valueobject;

import java.util.UUID;

public record CustomerCompanyId(String value) {
    public static CustomerCompanyId generate() {
        return new CustomerCompanyId(UUID.randomUUID().toString());
    }

    public static  CustomerCompanyId of(UUID uuid) {
        return new CustomerCompanyId(uuid.toString());
    }
}
