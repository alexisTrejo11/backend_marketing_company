package at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject;

import java.time.LocalDate;
import java.util.Optional;

import at.backend.MarketingCompany.crm.deal.core.domain.exceptions.DealValidationException;

public record ContractPeriod(LocalDate startDate, Optional<LocalDate> endDate) {
  public static ContractPeriod none() {
    return new ContractPeriod(null, Optional.empty());
  }

  public void validate() {
    if (startDate == null) {
      throw new DealValidationException("Contract period start date cannot be null");
    }
    endDate.ifPresent(ed -> {
      if (ed.isBefore(startDate)) {
        throw new DealValidationException("Contract period end date cannot be before start date");
      }
    });
  }
}
