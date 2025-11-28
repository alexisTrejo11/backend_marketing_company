package at.backend.MarketingCompany.crm.opportunity.application.queries;

import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityStage;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public record GetOpportunitiesByStagesQuery(Set<OpportunityStage> stages, Pageable pageable) {}
