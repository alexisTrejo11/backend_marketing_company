package at.backend.MarketingCompany.crm.opportunity.core.application.commands;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.Amount;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.ExpectedCloseDate;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;

public record UpdateOpportunityDetailsCommand(
    OpportunityId opportunityId,
    String title,
    Amount amount,
    ExpectedCloseDate expectedCloseDate) {
}
