package at.backend.MarketingCompany.crm.quote.core.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.Amount;
import at.backend.MarketingCompany.crm.quote.core.domain.model.QuoteItem;

public final class Discount {
  private final BigDecimal value;

  public static final Discount ZERO = new Discount(BigDecimal.ZERO);

  private Discount(BigDecimal percentage) {
    this.value = percentage;
  }

  public static Discount calculate(List<QuoteItem> items) {
    if (items == null || items.isEmpty()) {
      return ZERO;
    }

    Amount totalDiscount = Amount.ZERO;
    for (QuoteItem item : items) {
      if (item != null && item.getDiscountAmount() != null) {
        totalDiscount = totalDiscount.add(item.getDiscountAmount());
      }
    }

    Amount totalDiscountValue = totalDiscount
        .multiply(new BigDecimal("100"))
        .divide(new BigDecimal(items.size()));

    return new Discount(totalDiscountValue.value());
  }

  public static Discount create(String percentage) {
    if (percentage == null || percentage.trim().isEmpty()) {
      return ZERO;
    }
    return new Discount(new BigDecimal(percentage));
  }

  public static Discount create(BigDecimal percentage) {
    if (percentage == null) {
      return ZERO;
    }
    return new Discount(percentage);
  }

  public static Amount calculateDiscountAmount(Amount amount, BigDecimal percentage) {
    Objects.requireNonNull(amount, "Amount cannot be null");

    BigDecimal validPercentage = new Discount(percentage).value;
    return amount.multiply(new BigDecimal("100")
        .subtract(validPercentage))
        .divide(new BigDecimal("100"));
  }

  private BigDecimal validateAndScale(BigDecimal percentage) {
    if (percentage == null) {
      throw new IllegalArgumentException("Discount percentage cannot be null");
    }
    if (percentage.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Discount must be greater than or equal to 0");
    }
    if (percentage.compareTo(new BigDecimal("100")) > 0) {
      throw new IllegalArgumentException("Discount cannot be greater than 100%");
    }
    return percentage.setScale(2, RoundingMode.HALF_UP);
  }

  public BigDecimal percentage() {
    return value;
  }

  public BigDecimal decimalValue() {
    return value.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
  }

  public Amount applyTo(Amount amount) {
    Objects.requireNonNull(amount, "Amount cannot be null");
    Amount percentageToRemove = amount.multiply(new BigDecimal("100").subtract(value));

    return amount.subtract(percentageToRemove);

  }

  public static Discount random() {
    return new Discount(new BigDecimal(Math.random() * 90).setScale(2, RoundingMode.HALF_UP));
  }

  public boolean hasDiscount() {
    return value.compareTo(BigDecimal.ZERO) > 0;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Discount discount = (Discount) o;
    return value.compareTo(discount.value) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return value.toString() + "%";
  }
}
