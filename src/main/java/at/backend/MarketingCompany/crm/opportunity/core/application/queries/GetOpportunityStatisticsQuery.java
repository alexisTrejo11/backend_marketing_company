package at.backend.MarketingCompany.crm.opportunity.core.application.queries;

import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import org.springframework.data.domain.Pageable;

public record GetOpportunityStatisticsQuery(CustomerCompanyId customerCompanyId, Pageable pageable) {
}
