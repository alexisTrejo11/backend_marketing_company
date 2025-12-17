package at.backend.MarketingCompany.crm.tasks.core.application.commands;

import at.backend.MarketingCompany.crm.tasks.core.domain.entity.valueobject.DueDate;
import at.backend.MarketingCompany.crm.tasks.core.domain.entity.valueobject.TaskId;

public record ChangeTaskDueDateCommand(
    TaskId taskId,
    DueDate dueDate
) {}
