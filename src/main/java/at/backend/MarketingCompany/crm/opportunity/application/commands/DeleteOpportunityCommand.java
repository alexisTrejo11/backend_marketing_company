package at.backend.MarketingCompany.crm.opportunity.application.commands;

import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;

public record DeleteOpportunityCommand(OpportunityId opportunityId) {
    public static DeleteOpportunityCommand from(String id) {
        return new DeleteOpportunityCommand(new OpportunityId(id));
    }
}
