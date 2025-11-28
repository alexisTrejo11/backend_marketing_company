package at.backend.MarketingCompany.crm.opportunity.application.commands;

import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityAmount;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.ExpectedCloseDate;
import at.backend.MarketingCompany.customer.domain.ValueObjects.CustomerId;

public record CreateOpportunityCommand(
    CustomerId customerId,
    String title,
    OpportunityAmount amount,
    ExpectedCloseDate expectedCloseDate
) {}

