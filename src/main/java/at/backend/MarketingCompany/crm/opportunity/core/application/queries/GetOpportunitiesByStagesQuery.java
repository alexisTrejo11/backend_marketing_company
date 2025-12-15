package at.backend.MarketingCompany.crm.opportunity.core.application.queries;

import org.springframework.data.domain.Pageable;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityStage;

import java.util.List;
import java.util.Set;

public record GetOpportunitiesByStagesQuery(Set<OpportunityStage> stages, Pageable pageable) {
}
