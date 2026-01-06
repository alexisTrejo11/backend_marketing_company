package at.backend.MarketingCompany.crm.quote.core.domain.valueobject;

import java.math.BigDecimal;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.Amount;

public record Discount(BigDecimal value) {
  public static final Discount ZERO = new Discount(BigDecimal.ZERO);

  public Discount {
    if (value.compareTo(BigDecimal.ZERO) < 0 || value.compareTo(new BigDecimal("100")) > 0) {
      throw new IllegalArgumentException("Discount must be between 0 and 100");
    }
  }

  public Amount calculateDiscountAmount(Amount amount) {
    return new Amount(amount.value().multiply(value).divide(new BigDecimal("100")));
  }

  public BigDecimal percentage() {
    return value;
  }
}
