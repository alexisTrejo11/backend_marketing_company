package at.backend.MarketingCompany.crm.opportunity.core.application.queries;

import java.util.List;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityStage;

public record GetOpportunityByIdQuery(OpportunityId opportunityId) {
  public static GetOpportunityByIdQuery from(String id) {
    return new GetOpportunityByIdQuery(OpportunityId.of(id));
  }
}
