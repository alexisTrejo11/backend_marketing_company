package at.backend.MarketingCompany.crm.deal.application.queries;

import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.EmployeeId;

import java.util.List;

public record SearchDealsQuery(
    String searchTerm,
    EmployeeId campaignManagerId,
    List<String> statuses
) {}