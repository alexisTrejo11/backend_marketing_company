package at.backend.MarketingCompany.crm.deal.domain.entity.valueobject;

import at.backend.MarketingCompany.crm.deal.domain.exceptions.DealValidationException;

import java.time.LocalDate;
import java.util.Optional;

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