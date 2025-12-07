package at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject;

import at.backend.MarketingCompany.customer.domain.valueobject.CustomerId;
import lombok.Builder;

@Builder
public record CreateOpportunityParams(
    CustomerId customerId,
    String title,
    Amount amount,
    ExpectedCloseDate expectedCloseDate) {
}
