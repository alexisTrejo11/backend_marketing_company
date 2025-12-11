package at.backend.MarketingCompany.crm.deal.application.queries;

import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;

import java.util.UUID;

public record GetDealsByCustomerQuery(CustomerCompanyId customerCompanyId) {
  public static GetDealsByCustomerQuery from(UUID id) {
    return new GetDealsByCustomerQuery(new CustomerCompanyId(id.toString()));
  }
}
