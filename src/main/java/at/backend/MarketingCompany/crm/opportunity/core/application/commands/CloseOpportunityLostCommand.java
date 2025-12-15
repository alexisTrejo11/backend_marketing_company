package at.backend.MarketingCompany.crm.opportunity.core.application.commands;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;

public record CloseOpportunityLostCommand(OpportunityId opportunityId) {
  public static CloseOpportunityLostCommand from(String id) {
    return new CloseOpportunityLostCommand(new OpportunityId(id));
  }
}
