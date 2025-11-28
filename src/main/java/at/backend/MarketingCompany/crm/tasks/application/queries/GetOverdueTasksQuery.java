package at.backend.MarketingCompany.crm.tasks.application.queries;

import org.springframework.data.domain.Pageable;

public record GetOverdueTasksQuery(Pageable pageable) {}
