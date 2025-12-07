package at.backend.MarketingCompany.customer.application.dto.query;

import at.backend.MarketingCompany.customer.domain.valueobject.CustomerId;

public record IsCustomerBlockedQuery(CustomerId id) {
  public static IsCustomerBlockedQuery of(String id) {
    return new IsCustomerBlockedQuery(CustomerId.of(id));
  }
}
