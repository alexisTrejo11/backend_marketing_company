package at.backend.MarketingCompany.crm.deal.v2.application.dto.query;

import java.time.LocalDate;

public record GetDealsByDateRangeQuery(
    LocalDate startDate,
    LocalDate endDate
) {}