package at.backend.MarketingCompany.crm.opportunity.infrastructure.graphql.dto.input;

import at.backend.MarketingCompany.common.utils.PageInput;
import at.backend.MarketingCompany.crm.opportunity.application.queries.GetOpportunityStatisticsQuery;
import at.backend.MarketingCompany.customer.domain.ValueObjects.CustomerId;

public record GetOpportunityStatisticsInput(String customerId, PageInput pageInput) {
    public GetOpportunityStatisticsQuery toQuery() {
        return new GetOpportunityStatisticsQuery(
                new CustomerId(customerId),
                pageInput.toPageable()
        );
    }
}
