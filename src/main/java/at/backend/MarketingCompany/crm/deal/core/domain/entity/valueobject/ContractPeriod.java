package at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject;

import java.time.LocalDate;
import java.util.Optional;

import at.backend.MarketingCompany.crm.deal.core.domain.exceptions.DealValidationException;

public record ContractPeriod(LocalDate startDate, Optional<LocalDate> endDate) {
  public ContractPeriod {
    if (startDate == null) {
      throw new DealValidationException("Start date is required.");
    }

    if (endDate.isPresent() && endDate.get().isBefore(startDate)) {
      throw new DealValidationException("End date must be after start date.");
    }
  }
}
