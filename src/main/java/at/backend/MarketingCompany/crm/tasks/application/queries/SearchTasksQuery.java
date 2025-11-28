package at.backend.MarketingCompany.crm.tasks.application.queries;

import at.backend.MarketingCompany.crm.shared.enums.TaskPriority;
import at.backend.MarketingCompany.crm.shared.enums.TaskStatus;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public record SearchTasksQuery(
    String searchTerm,
    Set<TaskStatus> statuses,
    Set<TaskPriority> priorities,
    String customerId,
    String assigneeId,
    Boolean overdueOnly,
    Pageable pageable
) {}
