package at.backend.MarketingCompany.crm.tasks.core.application.commands;

import at.backend.MarketingCompany.crm.tasks.core.domain.entity.valueobject.TaskId;

public record DeleteTaskCommand(TaskId taskId) {}
