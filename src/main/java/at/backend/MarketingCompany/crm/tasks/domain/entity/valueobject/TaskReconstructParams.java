package at.backend.MarketingCompany.crm.tasks.domain.entity.valueobject;

import at.backend.MarketingCompany.crm.shared.enums.TaskPriority;
import at.backend.MarketingCompany.crm.shared.enums.TaskStatus;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.EmployeeId;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TaskReconstructParams(
    TaskId id,
    CustomerCompanyId customerCompanyId,
    OpportunityId opportunityId,
    String title,
    String description,
    DueDate dueDate,
    TaskStatus status,
    TaskPriority priority,
    EmployeeId assignedTo,
    Integer version,
    LocalDateTime deletedAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {
}
