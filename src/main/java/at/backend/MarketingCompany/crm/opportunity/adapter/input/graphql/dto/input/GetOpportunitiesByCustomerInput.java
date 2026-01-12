package at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.crm.opportunity.core.application.queries.GetOpportunitiesByCustomerQuery;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.shared.dto.PageInput;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record GetOpportunitiesByCustomerInput(
		@NotNull @Positive Long customerId,
		@NotNull PageInput pageInput) {

  public GetOpportunitiesByCustomerQuery toQuery() {
    return new GetOpportunitiesByCustomerQuery(
        new CustomerCompanyId(customerId),
        pageInput.toPageable());
  }
}
