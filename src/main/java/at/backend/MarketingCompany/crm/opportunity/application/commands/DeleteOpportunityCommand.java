package at.backend.MarketingCompany.crm.opportunity.application.commands;

import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;

public record DeleteOpportunityCommand(OpportunityId opportunityId) {}
