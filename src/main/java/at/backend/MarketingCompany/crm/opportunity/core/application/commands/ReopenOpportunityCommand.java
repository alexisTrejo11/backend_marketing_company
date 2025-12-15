package at.backend.MarketingCompany.crm.opportunity.core.application.commands;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;

public record ReopenOpportunityCommand(OpportunityId opportunityId) {
  public static ReopenOpportunityCommand from(String id) {
    return new ReopenOpportunityCommand(new OpportunityId(id));
  }
}
