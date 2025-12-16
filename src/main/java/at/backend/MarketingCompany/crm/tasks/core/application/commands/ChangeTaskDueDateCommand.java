package at.backend.MarketingCompany.crm.tasks.core.application.commands;

import at.backend.MarketingCompany.crm.tasks.core.domain.entity.valueobject.DueDate;

public record ChangeTaskDueDateCommand(
    String taskId,
    DueDate dueDate
) {}
