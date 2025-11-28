package at.backend.MarketingCompany.customer.domain.ValueObjects;

import java.util.UUID;

public record CustomerId(String value) {
    public static CustomerId of(UUID id) {
        return new CustomerId(id.toString());
    }

    public static CustomerId generate() {
        return new CustomerId(UUID.randomUUID().toString());
    }
}
