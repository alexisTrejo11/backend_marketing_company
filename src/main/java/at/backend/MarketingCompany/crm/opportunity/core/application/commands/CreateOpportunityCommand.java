package at.backend.MarketingCompany.crm.opportunity.core.application.commands;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.CreateOpportunityParams;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.Amount;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.ExpectedCloseDate;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;

public record CreateOpportunityCommand(
    CustomerCompanyId customerCompanyId,
    String title,
    Amount amount,
    ExpectedCloseDate expectedCloseDate) {

  public CreateOpportunityParams toCreateParams() {
    return CreateOpportunityParams.builder()
        .customerCompanyId(customerCompanyId)
        .title(title)
        .amount(amount)
        .expectedCloseDate(expectedCloseDate)
        .build();
  }
}
