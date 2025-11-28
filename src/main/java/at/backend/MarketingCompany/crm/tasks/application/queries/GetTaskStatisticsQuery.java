package at.backend.MarketingCompany.crm.tasks.application.queries;


import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.EmployeeId;
import at.backend.MarketingCompany.customer.domain.ValueObjects.CustomerId;

public record GetTaskStatisticsQuery(
    CustomerId customerId,
    EmployeeId assigneeId
) {}
