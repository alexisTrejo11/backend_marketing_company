package at.backend.MarketingCompany.crm.opportunity.core.application.commands;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;

public record DeleteOpportunityCommand(OpportunityId opportunityId) {
  public static DeleteOpportunityCommand from(String id) {
    return new DeleteOpportunityCommand(new OpportunityId(id));
  }
}
