package at.backend.MarketingCompany.crm.quote.core.domain.valueobject;

import at.backend.MarketingCompany.shared.domain.NumericId;

public class QuoteId extends NumericId {
  public QuoteId(Long value) {
    super(value);
  }

  public static QuoteId of(String id) {
    return NumericId.fromString(id, QuoteId::new);
  }

  // Database will generate the ID
  public static QuoteId generate() {
    return new QuoteId(0L);
  }
}
