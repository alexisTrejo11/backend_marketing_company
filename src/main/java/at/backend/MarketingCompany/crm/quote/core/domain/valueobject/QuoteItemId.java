package at.backend.MarketingCompany.crm.quote.core.domain.valueobject;

import java.util.UUID;

public record QuoteItemId(String value) {
  public static QuoteItemId of(String id) {
    return new QuoteItemId(id);
  }

  public static QuoteItemId generate() {
    return new QuoteItemId(UUID.randomUUID().toString());
  }
}
