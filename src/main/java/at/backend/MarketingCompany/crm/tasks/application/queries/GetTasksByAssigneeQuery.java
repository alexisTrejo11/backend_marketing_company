package at.backend.MarketingCompany.crm.tasks.application.queries;

import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.EmployeeId;
import org.springframework.data.domain.Pageable;

public record GetTasksByAssigneeQuery(EmployeeId assigneeId, Pageable pageable) {}
