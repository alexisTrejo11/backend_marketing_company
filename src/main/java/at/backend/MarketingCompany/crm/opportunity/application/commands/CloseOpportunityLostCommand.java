package at.backend.MarketingCompany.crm.opportunity.application.commands;

import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;

public record CloseOpportunityLostCommand(OpportunityId opportunityId) {
    public static CloseOpportunityLostCommand from(String id) {
        return new CloseOpportunityLostCommand(new OpportunityId(id));
    }
}
