package at.backend.MarketingCompany.crm.tasks.application.queries;

import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.external.EmployeeId;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;

public record GetTaskStatisticsQuery(
    CustomerCompanyId customerCompanyId,
    EmployeeId assigneeId) {
}
