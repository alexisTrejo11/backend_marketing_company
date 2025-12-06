package at.backend.MarketingCompany.crm.quote.domain.valueobject;

import java.util.UUID;

public record QuoteId(String value) {
  public static QuoteId of(String id) {
    return new QuoteId(id);
  }

  public static QuoteId generate() {
    return new QuoteId(UUID.randomUUID().toString());
  }
}
