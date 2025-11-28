package at.backend.MarketingCompany.crm.tasks.application.commands;

import at.backend.MarketingCompany.crm.Utils.enums.TaskPriority;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.*;
import at.backend.MarketingCompany.crm.tasks.domain.entity.valueobject.DueDate;

import java.time.LocalDateTime;

public record CreateTaskCommand(
    CustomerId customerId,
    OpportunityId opportunityId,
    String title,
    String description,
    DueDate dueDate,
    TaskPriority priority,
    EmployeeId assignedTo
) {}

