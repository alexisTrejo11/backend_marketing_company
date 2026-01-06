package at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject;

import at.backend.MarketingCompany.crm.opportunity.core.domain.exceptions.OpportunityValidationException;

public record LossReason(String value, String details) {

  public static final LossReason NONE = new LossReason("", "");

  public void validate() {
    if (value == null || value.isBlank()) {
      throw new OpportunityValidationException("Loss reason cannot be null or blank");
    }
    if (details != null && details.length() > 500) {
      throw new OpportunityValidationException("Loss reason details cannot exceed 500 characters");
    }
  }

  public static LossReason create(String value, String details) {
    LossReason lossReason = new LossReason(value, details);
    lossReason.validate();
    return lossReason;
  }

  public boolean isNone() {
    return this.equals(NONE);
  }
}
