package at.backend.MarketingCompany.crm.opportunity.application.commands;

import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;

public record MoveToProposalCommand(OpportunityId opportunityId) {
    public static MoveToProposalCommand from(String id) {
        return new MoveToProposalCommand(new OpportunityId(id));
    }
}
