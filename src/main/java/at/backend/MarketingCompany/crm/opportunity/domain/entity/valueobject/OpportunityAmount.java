package at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject;

import at.backend.MarketingCompany.crm.opportunity.domain.exceptions.OpportunityValidationException;

import java.math.BigDecimal;

public record OpportunityAmount(BigDecimal value) {
    public OpportunityAmount {
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

    public static OpportunityAmount from(BigDecimal amount) {
        return amount != null ? new OpportunityAmount(amount) : null;
    }
}