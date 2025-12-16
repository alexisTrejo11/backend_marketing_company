package at.backend.MarketingCompany.crm.tasks.core.application.queries;

import org.springframework.data.domain.Pageable;

import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.external.EmployeeId;

public record GetTasksByAssigneeQuery(EmployeeId assigneeId, Pageable pageable) {
}
