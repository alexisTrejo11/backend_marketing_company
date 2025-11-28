package at.backend.MarketingCompany.crm.tasks.application.queries;

import at.backend.MarketingCompany.crm.Utils.enums.TaskPriority;

import java.util.List;

public record GetTasksByPriorityQuery(List<TaskPriority> priorities) {}
