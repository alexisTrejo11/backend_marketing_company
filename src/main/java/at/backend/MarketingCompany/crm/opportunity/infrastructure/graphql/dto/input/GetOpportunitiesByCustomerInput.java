package at.backend.MarketingCompany.crm.opportunity.infrastructure.graphql.dto.input;

import at.backend.MarketingCompany.common.utils.PageInput;
import at.backend.MarketingCompany.crm.opportunity.application.queries.GetOpportunitiesByCustomerQuery;
import at.backend.MarketingCompany.crm.opportunity.application.queries.GetOpportunitiesByStageQuery;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityStage;
import at.backend.MarketingCompany.customer.domain.ValueObjects.CustomerId;

public record GetOpportunitiesByCustomerInput(String customerId, PageInput pageInput) {

    public GetOpportunitiesByCustomerQuery toQuery() {
        return new GetOpportunitiesByCustomerQuery(
                new CustomerId(customerId),
                pageInput.toPageable()
        );
    }
}


