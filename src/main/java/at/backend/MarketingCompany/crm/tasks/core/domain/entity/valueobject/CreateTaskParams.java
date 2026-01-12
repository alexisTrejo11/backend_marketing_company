package at.backend.MarketingCompany.crm.tasks.core.domain.entity.valueobject;

import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.external.EmployeeId;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import lombok.Builder;

@Builder
public record CreateTaskParams(
    CustomerCompanyId customerCompanyId,
    OpportunityId opportunityId,
    String title,
    String description,
    DueDate dueDate,
    TaskPriority priority,
    EmployeeId assignedTo) {
}
