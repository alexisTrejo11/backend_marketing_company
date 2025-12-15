package at.backend.MarketingCompany.crm.opportunity.core.application.commands;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.Amount;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;

public record UpdateOpportunityAmountCommand(
    OpportunityId opportunityId,
    Amount amount) {
}
