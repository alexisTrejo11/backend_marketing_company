package at.backend.MarketingCompany.crm.tasks.domain.entity.valueobject;

import at.backend.MarketingCompany.crm.Utils.enums.TaskPriority;
import at.backend.MarketingCompany.crm.Utils.enums.TaskStatus;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.EmployeeId;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.OpportunityId;
import at.backend.MarketingCompany.customer.domain.ValueObjects.CustomerId;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TaskReconstructParams(
    TaskId id,
    CustomerId customerId,
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
    LocalDateTime updatedAt
) {}