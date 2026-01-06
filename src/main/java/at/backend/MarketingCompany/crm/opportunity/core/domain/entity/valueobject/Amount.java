package at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Amount(BigDecimal value) {

  public static final Amount ZERO = new Amount(BigDecimal.ZERO);

  public Amount {
    value = value.setScale(2, RoundingMode.HALF_UP);
  }

  public static Amount create(BigDecimal value) {
    Amount amount = new Amount(value);
    amount.validate();
    return amount;
  }

  public void validate() {
    if (value == null) {
      throw new IllegalArgumentException("Amount cannot be null");
    }
    if (value.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Amount cannot be negative");
    }
  }

  public Amount add(Amount other) {
    return new Amount(this.value.add(other.value));
  }

  public Amount subtract(Amount other) {
    return new Amount(this.value.subtract(other.value));
  }

  public Amount multiply(BigDecimal multiplier) {
    return new Amount(this.value.multiply(multiplier).setScale(2, RoundingMode.HALF_UP));
  }

  public boolean isGreaterThan(Amount other) {
    return this.value.compareTo(other.value) > 0;
  }

  public boolean isPositive() {
    return this.value.compareTo(BigDecimal.ZERO) > 0;
  }
}
