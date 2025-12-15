package at.backend.MarketingCompany.crm.deal.core.application.queries;

import java.time.LocalDate;

public record GetDealsByDateRangeQuery(
    LocalDate startDate,
    LocalDate endDate) {
}
