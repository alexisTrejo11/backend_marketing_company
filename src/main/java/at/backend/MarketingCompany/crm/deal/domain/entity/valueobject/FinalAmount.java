package at.backend.MarketingCompany.crm.deal.domain.entity.valueobject;

import at.backend.MarketingCompany.crm.deal.domain.exceptions.DealValidationException;

import java.math.BigDecimal;

public record FinalAmount(BigDecimal value) {
    public FinalAmount {
        if (value == null) {
            throw new DealValidationException("Final amount is required.");
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new DealValidationException("Final amount cannot be negative.");
        }
    }

    public static FinalAmount zero() {
        return new FinalAmount(BigDecimal.ZERO);
    }

    public boolean isPositive() {
        return value.compareTo(BigDecimal.ZERO) > 0;
    }
}