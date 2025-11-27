package at.backend.MarketingCompany.crm.deal.application.queries;

import java.time.LocalDate;

public record GetDealsByDateRangeQuery(
    LocalDate startDate,
    LocalDate endDate
) {}