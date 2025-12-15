package at.backend.MarketingCompany.crm.opportunity.core.application.queries;

import org.springframework.data.domain.Pageable;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityStage;

public record GetOpportunitiesByStageQuery(OpportunityStage stage, Pageable pageable) {
}
