package at.backend.MarketingCompany.crm.opportunity.core.application.queries;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;

public record GetOpportunityByIdQuery(OpportunityId opportunityId) {
  public static GetOpportunityByIdQuery from(String id) {
    return new GetOpportunityByIdQuery(OpportunityId.of(id));
  }
}
