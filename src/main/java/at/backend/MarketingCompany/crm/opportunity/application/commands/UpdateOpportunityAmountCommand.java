package at.backend.MarketingCompany.crm.opportunity.application.commands;

import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityAmount;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;

public record UpdateOpportunityAmountCommand(
    OpportunityId opportunityId,
    OpportunityAmount amount
) {}
