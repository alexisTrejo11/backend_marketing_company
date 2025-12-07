package at.backend.MarketingCompany.crm.tasks.application.commands;

import at.backend.MarketingCompany.crm.shared.enums.TaskPriority;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.EmployeeId;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.tasks.domain.entity.valueobject.DueDate;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerId;

public record CreateTaskCommand(
    CustomerId customerId,
    OpportunityId opportunityId,
    String title,
    String description,
    DueDate dueDate,
    TaskPriority priority,
    EmployeeId assignedTo) {
}
