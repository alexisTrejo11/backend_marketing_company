package at.backend.MarketingCompany.crm.opportunity.infrastructure.graphql.dto.input;

import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.shared.dto.PageInput;
import at.backend.MarketingCompany.crm.opportunity.application.queries.GetOpportunityStatisticsQuery;

public record GetOpportunityStatisticsInput(String customerId, PageInput pageInput) {
  public GetOpportunityStatisticsQuery toQuery() {
    return new GetOpportunityStatisticsQuery(
        new CustomerCompanyId(customerId),
        pageInput.toPageable());
  }
}
