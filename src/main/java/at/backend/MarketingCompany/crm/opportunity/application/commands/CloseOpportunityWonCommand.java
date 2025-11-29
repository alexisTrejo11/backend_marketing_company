package at.backend.MarketingCompany.crm.opportunity.application.commands;

import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;

public record CloseOpportunityWonCommand(OpportunityId opportunityId) {
    public static CloseOpportunityWonCommand from(String id) {
        return new CloseOpportunityWonCommand(new OpportunityId(id));
    }
}
