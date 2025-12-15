package at.backend.MarketingCompany.crm.deal.core.application.queries;

import java.util.List;

import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.external.EmployeeId;

public record SearchDealsQuery(
    String searchTerm,
    EmployeeId campaignManagerId,
    List<String> statuses) {
}
