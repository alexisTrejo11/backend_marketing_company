package at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.crm.opportunity.core.application.queries.GetOpportunityStatisticsQuery;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.shared.dto.PageInput;

public record GetOpportunityStatisticsInput(String customerId, PageInput pageInput) {
  public GetOpportunityStatisticsQuery toQuery() {
    return new GetOpportunityStatisticsQuery(
        new CustomerCompanyId(customerId),
        pageInput.toPageable());
  }
}
