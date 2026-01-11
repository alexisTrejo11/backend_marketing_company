package at.backend.MarketingCompany.crm.quote.core.application.queries;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;

public record GetQuotesByOpportunityQuery(OpportunityId opportunityId) {
  public static GetQuotesByOpportunityQuery from(String opportunityId) {
    return new GetQuotesByOpportunityQuery(
        OpportunityId.of(opportunityId));
  }
}
