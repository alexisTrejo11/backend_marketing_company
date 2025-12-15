package at.backend.MarketingCompany.crm.opportunity.core.application.commands;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;

public record MoveToNegotiationCommand(OpportunityId opportunityId) {
  public static MoveToNegotiationCommand from(String id) {
    return new MoveToNegotiationCommand(new OpportunityId(id));
  }
}
