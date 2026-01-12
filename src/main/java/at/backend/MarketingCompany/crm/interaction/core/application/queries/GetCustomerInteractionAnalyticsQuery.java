package at.backend.MarketingCompany.crm.interaction.core.application.queries;

import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;

public record GetCustomerInteractionAnalyticsQuery(CustomerCompanyId customerId) {
	public static GetCustomerInteractionAnalyticsQuery from(String customerId) {
		return new GetCustomerInteractionAnalyticsQuery(CustomerCompanyId.of(customerId));
	}
}
