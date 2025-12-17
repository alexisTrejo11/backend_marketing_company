package at.backend.MarketingCompany.crm.opportunity.core.application.commands;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;

public record CloseOpportunityWonCommand(OpportunityId opportunityId) {
  public static CloseOpportunityWonCommand from(String id) {
    return new CloseOpportunityWonCommand(OpportunityId.of(id));
  }
}
