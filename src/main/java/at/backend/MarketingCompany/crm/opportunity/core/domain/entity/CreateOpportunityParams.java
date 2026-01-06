package at.backend.MarketingCompany.crm.opportunity.core.domain.entity;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.Amount;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.ExpectedCloseDate;
import at.backend.MarketingCompany.crm.opportunity.core.domain.exceptions.OpportunityValidationException;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import lombok.Builder;

@Builder
public record CreateOpportunityParams(
    CustomerCompanyId customerCompanyId,
    String title,
    Amount amount,
    ExpectedCloseDate expectedCloseDate) {

  public CreateOpportunityParams {
    if (customerCompanyId == null) {
      throw new OpportunityValidationException("Customer ID is required");
    }
    if (title == null || title.trim().isEmpty()) {
      throw new OpportunityValidationException("Opportunity title is required");
    }
    if (expectedCloseDate == null) {
      throw new OpportunityValidationException("Expected close date is required");
    }
  }

}
