package at.backend.MarketingCompany.crm.tasks.application.queries;

import at.backend.MarketingCompany.crm.shared.enums.TaskPriority;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public record GetTasksByPriorityQuery(Set<TaskPriority> priorities, Pageable pageable) {}
