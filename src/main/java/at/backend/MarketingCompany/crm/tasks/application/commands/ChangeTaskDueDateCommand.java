package at.backend.MarketingCompany.crm.tasks.application.commands;

import at.backend.MarketingCompany.crm.tasks.domain.entity.valueobject.DueDate;

public record ChangeTaskDueDateCommand(
    String taskId,
    DueDate dueDate
) {}
