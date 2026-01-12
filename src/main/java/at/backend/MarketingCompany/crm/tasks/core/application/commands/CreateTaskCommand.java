package at.backend.MarketingCompany.crm.tasks.core.application.commands;

import at.backend.MarketingCompany.crm.tasks.core.domain.entity.valueobject.TaskPriority;
import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.external.EmployeeId;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.tasks.core.domain.entity.valueobject.DueDate;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;

public record CreateTaskCommand(
    CustomerCompanyId customerCompanyId,
    OpportunityId opportunityId,
    String title,
    String description,
    DueDate dueDate,
    TaskPriority priority,
    EmployeeId assignedTo) {
}
