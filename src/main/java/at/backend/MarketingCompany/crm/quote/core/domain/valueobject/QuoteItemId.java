package at.backend.MarketingCompany.crm.quote.core.domain.valueobject;

import at.backend.MarketingCompany.shared.domain.NumericId;

import java.util.UUID;


public class QuoteItemId extends NumericId {
    public QuoteItemId(Long value) {
        super(value);
    }

    public static QuoteItemId of(String id) {
        return NumericId.fromString(id, QuoteItemId::new);
    }

    // Database will generate the ID
    public static QuoteItemId generate() {
        return new QuoteItemId(null);
    }
}