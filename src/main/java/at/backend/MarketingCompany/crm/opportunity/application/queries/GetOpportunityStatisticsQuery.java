package at.backend.MarketingCompany.crm.opportunity.application.queries;

import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;
import org.springframework.data.domain.Pageable;

public record GetOpportunityStatisticsQuery(CustomerCompanyId customerCompanyId, Pageable pageable) {
}
