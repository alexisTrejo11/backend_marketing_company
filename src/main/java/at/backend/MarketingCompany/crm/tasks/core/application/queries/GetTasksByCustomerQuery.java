package at.backend.MarketingCompany.crm.tasks.core.application.queries;

import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import org.springframework.data.domain.Pageable;

public record GetTasksByCustomerQuery(CustomerCompanyId customerCompanyId, Pageable pageable) {
}
