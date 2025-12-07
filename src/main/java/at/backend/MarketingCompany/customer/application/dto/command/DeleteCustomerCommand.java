package at.backend.MarketingCompany.customer.application.dto.command;

import at.backend.MarketingCompany.customer.domain.valueobject.CustomerId;

public record DeleteCustomerCommand(CustomerId id) {
  public static DeleteCustomerCommand of(String id) {
    return new DeleteCustomerCommand(CustomerId.of(id));
  }
}
