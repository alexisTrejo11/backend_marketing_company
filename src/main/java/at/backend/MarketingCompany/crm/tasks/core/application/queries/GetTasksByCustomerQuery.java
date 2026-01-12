package at.backend.MarketingCompany.crm.tasks.core.application.queries;

import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import org.springframework.data.domain.Pageable;

public record GetTasksByCustomerQuery(CustomerCompanyId customerCompanyId, Pageable pageable) {

	public static GetTasksByCustomerQuery of(String customerCompanyId, Pageable pageable) {
		return new GetTasksByCustomerQuery(CustomerCompanyId.of(customerCompanyId), pageable);
	}
}
