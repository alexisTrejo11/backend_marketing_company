package at.backend.MarketingCompany.crm.opportunity.core.application.commands;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;

public record ReopenOpportunityCommand(OpportunityId opportunityId, String reason) {
  public static ReopenOpportunityCommand from(String id, String reason) {
    return new ReopenOpportunityCommand(OpportunityId.of(id), reason);
  }

  public ReopenOpportunityCommand {
    if (opportunityId == null) {
      throw new IllegalArgumentException("OpportunityId cannot be null");
    }
  }
}
