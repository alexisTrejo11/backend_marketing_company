package at.backend.MarketingCompany.crm.opportunity.core.application.commands;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;

public record MoveToProposalCommand(OpportunityId opportunityId) {
  public static MoveToProposalCommand from(String id) {
    return new MoveToProposalCommand(OpportunityId.of(id));
  }
}
