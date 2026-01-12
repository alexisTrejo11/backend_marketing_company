package at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject;

import at.backend.MarketingCompany.shared.domain.NumericId;

public class DealId extends NumericId {
  public DealId(Long value) {
    super(value);
  }

  public static DealId of(String id) {
    return NumericId.fromString(id, DealId::new);
  }

  // Database will generate the ID
  public static DealId generate() {
    return new DealId(0L);
  }
}
