package at.backend.MarketingCompany.crm.opportunity.application.queries;

import org.springframework.data.domain.Pageable;

public record GetOverdueOpportunitiesQuery(Pageable pageable) {}
