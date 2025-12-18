package at.backend.MarketingCompany.crm.deal.adapter.input.graphql.dto.request;

import at.backend.MarketingCompany.crm.deal.core.application.queries.GetDealsByCustomerQuery;
import at.backend.MarketingCompany.shared.dto.PageInput;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GetDealsByCustomerInput(@NotBlank String customerId, @NotNull PageInput pageInput) {

	public GetDealsByCustomerQuery toQuery() {
		return GetDealsByCustomerQuery.from(customerId, pageInput);
	}
}
