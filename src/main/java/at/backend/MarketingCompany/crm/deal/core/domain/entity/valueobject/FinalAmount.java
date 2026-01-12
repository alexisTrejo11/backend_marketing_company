package at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject;

import java.math.BigDecimal;

import at.backend.MarketingCompany.crm.deal.core.domain.exceptions.DealValidationException;

public record FinalAmount(BigDecimal value) {
  public FinalAmount {
    if (value == null) {
      throw new DealValidationException("Final amount is required.");
    }
    if (value.compareTo(BigDecimal.ZERO) < 0) {
      throw new DealValidationException("Final amount cannot be negative.");
    }
  }

  public static FinalAmount one() {
    return new FinalAmount(BigDecimal.ONE);
  }

  public boolean isPositive() {
    return value.compareTo(BigDecimal.ZERO) > 0;
  }
}
