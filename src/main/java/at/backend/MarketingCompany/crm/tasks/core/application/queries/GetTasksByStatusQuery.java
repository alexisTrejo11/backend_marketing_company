package at.backend.MarketingCompany.crm.tasks.core.application.queries;

import at.backend.MarketingCompany.crm.tasks.core.domain.entity.valueobject.TaskStatus;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public record GetTasksByStatusQuery(Set<TaskStatus> statuses, Pageable pageable) {}
