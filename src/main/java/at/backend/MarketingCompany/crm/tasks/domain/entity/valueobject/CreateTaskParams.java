package at.backend.MarketingCompany.crm.tasks.domain.entity.valueobject;

import at.backend.MarketingCompany.crm.Utils.enums.TaskPriority;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.EmployeeId;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.OpportunityId;
import at.backend.MarketingCompany.customer.domain.ValueObjects.CustomerId;
import lombok.Builder;

@Builder
public record CreateTaskParams(
    CustomerId customerId,
    OpportunityId opportunityId,
    String title,
    String description,
    DueDate dueDate,
    TaskPriority priority,
    EmployeeId assignedTo
) {}