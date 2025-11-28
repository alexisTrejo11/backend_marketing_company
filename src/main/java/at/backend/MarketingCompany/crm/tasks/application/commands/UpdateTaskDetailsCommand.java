package at.backend.MarketingCompany.crm.tasks.application.commands;

import at.backend.MarketingCompany.crm.shared.enums.TaskPriority;

public record UpdateTaskDetailsCommand(
    String taskId,
    String title,
    String description,
    TaskPriority priority
) {}
