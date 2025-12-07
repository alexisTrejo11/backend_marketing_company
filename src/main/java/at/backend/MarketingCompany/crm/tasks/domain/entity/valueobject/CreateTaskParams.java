package at.backend.MarketingCompany.crm.tasks.domain.entity.valueobject;

import at.backend.MarketingCompany.crm.shared.enums.TaskPriority;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerId;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.EmployeeId;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;
import lombok.Builder;

@Builder
public record CreateTaskParams(
    CustomerId customerId,
    OpportunityId opportunityId,
    String title,
    String description,
    DueDate dueDate,
    TaskPriority priority,
    EmployeeId assignedTo) {
}
