package at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject;

import at.backend.MarketingCompany.shared.domain.NumericId;

public class InteractionId extends NumericId {
  public InteractionId(Long value) {
    super(value);
  }

  public static InteractionId of(String id) {
    return NumericId.fromString(id, InteractionId::new);
  }

  // Database will generate the ID
  public static InteractionId generate() {
    return new InteractionId(0L);
  }
}
