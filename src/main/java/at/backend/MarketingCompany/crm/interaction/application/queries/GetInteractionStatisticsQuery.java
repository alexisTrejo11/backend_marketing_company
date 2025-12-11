package at.backend.MarketingCompany.crm.interaction.application.queries;

import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;

public record GetInteractionStatisticsQuery(CustomerCompanyId customerCompanyId) {
  public static GetInteractionStatisticsQuery from(String customerId) {
    return new GetInteractionStatisticsQuery(new CustomerCompanyId(customerId));
  }
}
