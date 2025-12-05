package at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject;

import at.backend.MarketingCompany.crm.opportunity.domain.exceptions.OpportunityValidationException;

import java.math.BigDecimal;

// TODO: Move to a shared kernel if used in multiple bounded contexts
public record Amount(BigDecimal value) {
    public Amount {
        if (value == null) {
            throw new OpportunityValidationException("Opportunity amount is required");
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new OpportunityValidationException("Opportunity amount cannot be negative");
        }
    }

    public boolean isPositive() {
        return value.compareTo(BigDecimal.ZERO) > 0;
    }

    public static Amount from(BigDecimal amount) {
        return amount != null ? new Amount(amount) : null;
    }

    public static Amount zero() {
        return new Amount(BigDecimal.ZERO);
    }
}