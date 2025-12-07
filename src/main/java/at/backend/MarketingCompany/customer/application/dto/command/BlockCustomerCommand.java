package at.backend.MarketingCompany.customer.application.dto.command;

import at.backend.MarketingCompany.customer.domain.valueobject.CustomerId;

public record BlockCustomerCommand(CustomerId id) {
  public static BlockCustomerCommand of(String id) {
    return new BlockCustomerCommand(CustomerId.of(id));
  }
}
