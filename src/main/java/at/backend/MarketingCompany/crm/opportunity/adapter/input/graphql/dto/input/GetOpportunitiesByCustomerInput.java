package at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.crm.opportunity.core.application.queries.GetOpportunitiesByCustomerQuery;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.shared.dto.PageInput;

public record GetOpportunitiesByCustomerInput(String customerId, PageInput pageInput) {

  public GetOpportunitiesByCustomerQuery toQuery() {
    return new GetOpportunitiesByCustomerQuery(
        new CustomerCompanyId(customerId),
        pageInput.toPageable());
  }
}
