package at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

import at.backend.MarketingCompany.crm.quote.core.domain.model.QuoteItem;

public record Amount(BigDecimal value) {

  public static final Amount ZERO = new Amount(BigDecimal.ZERO);

  public Amount {
    if (value != null) {
      value = value.setScale(2, RoundingMode.HALF_UP);
    }
  }

  public static Amount calculateSubTotal(List<QuoteItem> items) {
    if (items == null || items.isEmpty()) {
      return ZERO;
    }

    return items.stream()
        .filter(Objects::nonNull)
        .map(QuoteItem::getUnitPrice)
        .filter(Objects::nonNull)
        .reduce(Amount.ZERO, Amount::add);
  }

  public static Amount calculateTotal(List<QuoteItem> items) {
    if (items == null || items.isEmpty()) {
      return Amount.ZERO;
    }

    return items.stream()
        .filter(Objects::nonNull)
        .map(QuoteItem::getDiscountAmount)
        .filter(Objects::nonNull)
        .reduce(Amount.ZERO, Amount::add);
  }

  public static Amount create(BigDecimal value) {
    return new Amount(value);
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

  public Amount divide(BigDecimal divisor) {
    if (divisor.compareTo(BigDecimal.ZERO) == 0) {
      throw new IllegalArgumentException("Division by zero is not allowed");
    }
    return new Amount(this.value.divide(divisor, 2, RoundingMode.HALF_UP));
  }
}
