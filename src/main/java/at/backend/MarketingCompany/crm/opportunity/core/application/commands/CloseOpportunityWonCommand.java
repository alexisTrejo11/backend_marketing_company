package at.backend.MarketingCompany.crm.opportunity.core.application.commands;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;

public record CloseOpportunityWonCommand(OpportunityId opportunityId, String closeNotes) {
  public static CloseOpportunityWonCommand from(String id, String closeNotes) {
    return new CloseOpportunityWonCommand(OpportunityId.of(id), closeNotes);
  }

  public CloseOpportunityWonCommand {
    if (opportunityId == null) {
      throw new IllegalArgumentException("OpportunityId cannot be null");
    }
  }
}
