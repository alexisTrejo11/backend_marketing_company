package at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.crm.opportunity.core.application.queries.GetOpportunityStatisticsQuery;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import jakarta.validation.constraints.Positive;

public record GetOpportunityStatisticsInput(
    @Positive Long customerId) {
  public GetOpportunityStatisticsQuery toQuery() {
    return new GetOpportunityStatisticsQuery(
        customerId != null ? new CustomerCompanyId(customerId) : null);
  }
}
