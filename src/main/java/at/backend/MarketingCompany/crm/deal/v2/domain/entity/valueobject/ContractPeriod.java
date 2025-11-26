package at.backend.MarketingCompany.crm.deal.v2.domain.entity.valueobject;

import at.backend.MarketingCompany.crm.deal.v2.domain.exceptions.DealValidationException;

import java.time.LocalDate;

public record ContractPeriod(LocalDate startDate, LocalDate endDate) {
    public ContractPeriod {
        if (startDate == null) {
            throw new DealValidationException("Start date is required.");
        }
        if (endDate != null && endDate.isBefore(startDate)) {
            throw new DealValidationException("End date must be after start date.");
        }
    }
}