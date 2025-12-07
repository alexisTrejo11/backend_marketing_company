package at.backend.MarketingCompany.customer.application.dto.query;

import at.backend.MarketingCompany.customer.domain.valueobject.CustomerId;

public record GetCustomerByIdQuery(CustomerId id) {
  public static GetCustomerByIdQuery of(String id) {
    return new GetCustomerByIdQuery(CustomerId.of(id));
  }
}
