package at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject;

import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;
import lombok.Builder;

@Builder
public record CreateOpportunityParams(
    CustomerCompanyId customerCompanyId,
    String title,
    Amount amount,
    ExpectedCloseDate expectedCloseDate) {
}
