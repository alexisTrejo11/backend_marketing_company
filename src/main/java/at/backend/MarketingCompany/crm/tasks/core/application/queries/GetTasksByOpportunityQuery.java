package at.backend.MarketingCompany.crm.tasks.core.application.queries;

import org.springframework.data.domain.Pageable;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;

public record GetTasksByOpportunityQuery(OpportunityId opportunityId, Pageable pageable) {
}
