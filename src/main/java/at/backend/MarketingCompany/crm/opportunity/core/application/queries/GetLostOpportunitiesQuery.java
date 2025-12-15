package at.backend.MarketingCompany.crm.opportunity.core.application.queries;

import org.springframework.data.domain.Pageable;

public record GetLostOpportunitiesQuery(Pageable pageable) {
}
