package at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject;


import java.math.BigDecimal;
import java.math.RoundingMode;

public record Amount(BigDecimal value) {

    public static final Amount ZERO = new Amount(BigDecimal.ZERO);

    public Amount {
        if (value == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        value = value.setScale(2, RoundingMode.HALF_UP);
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
}
