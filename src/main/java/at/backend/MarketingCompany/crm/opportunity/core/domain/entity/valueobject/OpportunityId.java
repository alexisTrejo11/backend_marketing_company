package at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject;

import at.backend.MarketingCompany.shared.domain.exceptions.MissingFieldException;

import java.util.UUID;

public record OpportunityId(String value) {
  public OpportunityId {
    if (value == null || value.isBlank()) {
      throw new MissingFieldException("OpportunityId", "value");
    }
  }

  public static OpportunityId generate() {
    return new OpportunityId(UUID.randomUUID().toString());
  }

  public static OpportunityId from(String value) {
    return new OpportunityId(value);
  }
}
