package at.backend.MarketingCompany.crm.opportunity.application.commands;

import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;

public record QualifyOpportunityCommand(OpportunityId opportunityId) {
    public static QualifyOpportunityCommand from(String id) {
        return new QualifyOpportunityCommand(new OpportunityId(id));
    }
}
