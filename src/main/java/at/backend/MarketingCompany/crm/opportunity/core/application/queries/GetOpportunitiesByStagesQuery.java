package at.backend.MarketingCompany.crm.opportunity.core.application.queries;

import java.util.Set;

import org.springframework.data.domain.Pageable;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityStage;

public record GetOpportunitiesByStagesQuery(Set<OpportunityStage> stages, Pageable pageable) {
}
