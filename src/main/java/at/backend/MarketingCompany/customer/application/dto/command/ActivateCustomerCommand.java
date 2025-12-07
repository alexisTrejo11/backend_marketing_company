package at.backend.MarketingCompany.customer.application.dto.command;

import at.backend.MarketingCompany.customer.domain.valueobject.CustomerId;

public record ActivateCustomerCommand(CustomerId id) {
  public static ActivateCustomerCommand of(String id) {
    return new ActivateCustomerCommand(CustomerId.of(id));
  }
}
