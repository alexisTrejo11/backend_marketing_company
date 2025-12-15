package at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject;

import java.time.LocalDate;

import at.backend.MarketingCompany.crm.opportunity.core.domain.exceptions.OpportunityValidationException;

public record ExpectedCloseDate(LocalDate value) {

  public static ExpectedCloseDate create(LocalDate date) {
    if (date != null && date.isBefore(LocalDate.now())) {
      throw new OpportunityValidationException("Expected close date cannot be in the past");
    }

    return date != null ? new ExpectedCloseDate(date) : null;
  }

  public boolean isOverdue() {
    return value != null && value.isBefore(LocalDate.now());
  }

  public static ExpectedCloseDate from(LocalDate date) {
    return date != null ? new ExpectedCloseDate(date) : null;
  }
}
