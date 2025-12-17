package at.backend.MarketingCompany.crm.opportunity.core.domain.exceptions;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;

public class OpportunityNotFoundException extends RuntimeException {
  public OpportunityNotFoundException(OpportunityId opportunityId) {
    super("Opportunity not found with ID: " + opportunityId.asString());
  }
}
