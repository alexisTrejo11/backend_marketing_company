package at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject;

import at.backend.MarketingCompany.crm.opportunity.domain.exceptions.OpportunityValidationException;

import java.time.LocalDate;

public record ExpectedCloseDate(LocalDate value) {
    public ExpectedCloseDate {
        if (value != null && value.isBefore(LocalDate.now())) {
            throw new OpportunityValidationException("Expected close date cannot be in the past");
        }
    }

    public boolean isOverdue() {
        return value != null && value.isBefore(LocalDate.now());
    }

    public static ExpectedCloseDate from(LocalDate date) {
        return date != null ? new ExpectedCloseDate(date) : null;
    }
}