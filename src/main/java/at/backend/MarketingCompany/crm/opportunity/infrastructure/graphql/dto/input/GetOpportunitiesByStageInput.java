package at.backend.MarketingCompany.crm.opportunity.infrastructure.graphql.dto.input;

import at.backend.MarketingCompany.common.utils.PageInput;
import at.backend.MarketingCompany.crm.opportunity.application.queries.GetOpportunitiesByStageQuery;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityStage;

public record GetOpportunitiesByStageInput(String stage, PageInput pageInput) {

    public GetOpportunitiesByStageQuery toQuery() {
        return new GetOpportunitiesByStageQuery(
                OpportunityStage.valueOf(stage),
                pageInput.toPageable()
        );
    }
}
