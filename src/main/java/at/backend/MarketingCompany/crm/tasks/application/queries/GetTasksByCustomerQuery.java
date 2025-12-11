package at.backend.MarketingCompany.crm.tasks.application.queries;

import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;
import org.springframework.data.domain.Pageable;

public record GetTasksByCustomerQuery(CustomerCompanyId customerCompanyId, Pageable pageable) {
}
