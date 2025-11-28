package at.backend.MarketingCompany.crm.tasks.application.queries;

import at.backend.MarketingCompany.crm.Utils.enums.TaskPriority;
import at.backend.MarketingCompany.crm.Utils.enums.TaskStatus;

import java.util.List;

public record GetTaskByIdQuery(String taskId) {}

