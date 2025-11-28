package at.backend.MarketingCompany.crm.tasks.application.commands;

import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.EmployeeId;

public record AssignTaskCommand(
    String taskId,
    EmployeeId assignedTo
) {}
