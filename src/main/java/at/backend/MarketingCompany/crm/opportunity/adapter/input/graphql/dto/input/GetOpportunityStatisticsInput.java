package at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.crm.opportunity.core.application.queries.GetOpportunityStatisticsQuery;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.shared.dto.PageInput;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record GetOpportunityStatisticsInput(
		@NotNull @Positive Long customerId,
		@NotNull PageInput pageInput) {
  public GetOpportunityStatisticsQuery toQuery() {
    return new GetOpportunityStatisticsQuery(
        new CustomerCompanyId(customerId),
        pageInput.toPageable());
  }
}
