package at.backend.MarketingCompany.crm.tasks.core.application.commands;

import at.backend.MarketingCompany.crm.tasks.core.domain.entity.valueobject.TaskId;
import at.backend.MarketingCompany.crm.tasks.core.domain.entity.valueobject.TaskPriority;

public record UpdateTaskDetailsCommand(
    TaskId taskId,
    String title,
    String description,
    TaskPriority priority
) {}
