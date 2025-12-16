package at.backend.MarketingCompany.crm.tasks.core.application.commands;

import at.backend.MarketingCompany.crm.tasks.core.domain.entity.valueobject.TaskPriority;

public record UpdateTaskDetailsCommand(
    String taskId,
    String title,
    String description,
    TaskPriority priority
) {}
