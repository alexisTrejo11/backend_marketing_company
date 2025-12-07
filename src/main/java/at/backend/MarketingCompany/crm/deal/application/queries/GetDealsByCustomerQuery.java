package at.backend.MarketingCompany.crm.deal.application.queries;

import java.util.UUID;

import at.backend.MarketingCompany.customer.domain.valueobject.CustomerId;

public record GetDealsByCustomerQuery(CustomerId customerId) {
  public static GetDealsByCustomerQuery from(UUID id) {
    return new GetDealsByCustomerQuery(new CustomerId(id.toString()));
  }
}
