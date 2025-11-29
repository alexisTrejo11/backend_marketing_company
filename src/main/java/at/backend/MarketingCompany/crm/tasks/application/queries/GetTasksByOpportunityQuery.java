package at.backend.MarketingCompany.crm.tasks.application.queries;

import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;
import org.springframework.data.domain.Pageable;

public record GetTasksByOpportunityQuery(OpportunityId opportunityId, Pageable pageable) {}
