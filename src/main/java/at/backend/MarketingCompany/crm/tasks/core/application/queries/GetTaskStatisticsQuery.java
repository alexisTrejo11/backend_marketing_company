package at.backend.MarketingCompany.crm.tasks.core.application.queries;

import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.external.EmployeeId;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;

public record GetTaskStatisticsQuery(
    CustomerCompanyId customerCompanyId,
    EmployeeId assigneeId) {
}
