package at.backend.MarketingCompany.crm.deal.v2.application.dto.query;

import at.backend.MarketingCompany.crm.deal.v2.domain.entity.valueobject.external.EmployeeId;

import java.util.List;

public record SearchDealsQuery(
    String searchTerm,
    EmployeeId campaignManagerId,
    List<String> statuses
) {}