package at.backend.MarketingCompany.crm.tasks.application.commands;

import at.backend.MarketingCompany.crm.Utils.enums.TaskPriority;

public record UpdateTaskDetailsCommand(
    String taskId,
    String title,
    String description,
    TaskPriority priority
) {}
