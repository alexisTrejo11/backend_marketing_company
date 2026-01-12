package at.backend.MarketingCompany.crm.tasks.core.application.queries;

import at.backend.MarketingCompany.crm.tasks.core.domain.entity.valueobject.TaskPriority;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public record GetTasksByPriorityQuery(Set<TaskPriority> priorities, Pageable pageable) {}
