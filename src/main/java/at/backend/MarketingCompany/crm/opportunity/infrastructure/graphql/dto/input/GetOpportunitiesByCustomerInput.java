package at.backend.MarketingCompany.crm.opportunity.infrastructure.graphql.dto.input;

import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.shared.dto.PageInput;
import at.backend.MarketingCompany.crm.opportunity.application.queries.GetOpportunitiesByCustomerQuery;

public record GetOpportunitiesByCustomerInput(String customerId, PageInput pageInput) {

  public GetOpportunitiesByCustomerQuery toQuery() {
    return new GetOpportunitiesByCustomerQuery(
        new CustomerCompanyId(customerId),
        pageInput.toPageable());
  }
}
