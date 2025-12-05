package at.backend.MarketingCompany.crm.quote.domain.valueobject;

import java.util.UUID;

public record QuoteId(String value) {
    public static at.backend.MarketingCompany.customer.domain.ValueObjects.CustomerId of(String id) {
        return new at.backend.MarketingCompany.customer.domain.ValueObjects.CustomerId(id);
    }

    public static at.backend.MarketingCompany.customer.domain.ValueObjects.CustomerId generate() {
        return new at.backend.MarketingCompany.customer.domain.ValueObjects.CustomerId(UUID.randomUUID().toString());
    }
}
