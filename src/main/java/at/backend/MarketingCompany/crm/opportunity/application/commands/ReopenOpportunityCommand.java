package at.backend.MarketingCompany.crm.opportunity.application.commands;

import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;

public record ReopenOpportunityCommand(OpportunityId opportunityId) {
    public static ReopenOpportunityCommand from(String id) {
        return new ReopenOpportunityCommand(new OpportunityId(id));
    }
}
