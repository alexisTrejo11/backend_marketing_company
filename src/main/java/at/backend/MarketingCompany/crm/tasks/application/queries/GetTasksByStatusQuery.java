package at.backend.MarketingCompany.crm.tasks.application.queries;

import at.backend.MarketingCompany.crm.Utils.enums.TaskStatus;

import java.util.List;

public record GetTasksByStatusQuery(List<TaskStatus> statuses) {}
