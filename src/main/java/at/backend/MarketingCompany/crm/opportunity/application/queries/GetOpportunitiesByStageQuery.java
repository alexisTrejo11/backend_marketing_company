package at.backend.MarketingCompany.crm.opportunity.application.queries;

import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityStage;
import org.springframework.data.domain.Pageable;

public record GetOpportunitiesByStageQuery(OpportunityStage stage, Pageable pageable) {}
