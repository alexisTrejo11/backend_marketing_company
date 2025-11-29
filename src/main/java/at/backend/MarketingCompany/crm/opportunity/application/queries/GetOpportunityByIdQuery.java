package at.backend.MarketingCompany.crm.opportunity.application.queries;

import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityStage;

import java.util.List;

public record GetOpportunityByIdQuery(OpportunityId opportunityId) {
    public static GetOpportunityByIdQuery from(String id) {
        return new GetOpportunityByIdQuery(new OpportunityId(id));
    }
}

