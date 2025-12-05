package at.backend.MarketingCompany.crm.opportunity.application.commands;

import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.ExpectedCloseDate;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.Amount;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;

public record UpdateOpportunityDetailsCommand(
    OpportunityId opportunityId,
    String title,
    Amount amount,
    ExpectedCloseDate expectedCloseDate
) {}
