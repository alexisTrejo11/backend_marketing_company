package at.backend.MarketingCompany.crm.tasks.core.application.queries;

import org.springframework.data.domain.Pageable;

public record GetPendingTasksQuery(Pageable pageable) {}
