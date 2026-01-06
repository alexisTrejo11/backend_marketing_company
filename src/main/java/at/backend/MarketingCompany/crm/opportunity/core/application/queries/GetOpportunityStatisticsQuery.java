package at.backend.MarketingCompany.crm.opportunity.core.application.queries;

import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;

public record GetOpportunityStatisticsQuery(CustomerCompanyId customerCompanyId) {
}
