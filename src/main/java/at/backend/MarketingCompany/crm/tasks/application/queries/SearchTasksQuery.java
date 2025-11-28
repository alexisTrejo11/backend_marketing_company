package at.backend.MarketingCompany.crm.tasks.application.queries;

import at.backend.MarketingCompany.crm.Utils.enums.TaskPriority;
import at.backend.MarketingCompany.crm.Utils.enums.TaskStatus;

import java.util.List;

public record SearchTasksQuery(
    String searchTerm,
    List<TaskStatus> statuses,
    List<TaskPriority> priorities,
    String customerId,
    String assigneeId,
    Boolean overdueOnly
) {}
