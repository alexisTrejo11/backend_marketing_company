package at.backend.MarketingCompany.crm.opportunity.application.commands;

import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.ExpectedCloseDate;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityAmount;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;

public record UpdateOpportunityDetailsCommand(
    OpportunityId opportunityId,
    String title,
    OpportunityAmount amount,
    ExpectedCloseDate expectedCloseDate
) {}
