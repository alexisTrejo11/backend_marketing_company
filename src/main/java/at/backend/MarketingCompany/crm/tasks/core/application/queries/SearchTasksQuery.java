package at.backend.MarketingCompany.crm.tasks.core.application.queries;

import at.backend.MarketingCompany.crm.tasks.core.domain.entity.valueobject.TaskPriority;
import at.backend.MarketingCompany.crm.tasks.core.domain.entity.valueobject.TaskStatus;
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
