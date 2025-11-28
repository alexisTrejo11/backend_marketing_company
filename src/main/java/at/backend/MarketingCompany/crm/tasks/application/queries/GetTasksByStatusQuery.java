package at.backend.MarketingCompany.crm.tasks.application.queries;

import at.backend.MarketingCompany.crm.shared.enums.TaskStatus;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public record GetTasksByStatusQuery(Set<TaskStatus> statuses, Pageable pageable) {}
