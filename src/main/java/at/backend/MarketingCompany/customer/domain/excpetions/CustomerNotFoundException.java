package at.backend.MarketingCompany.customer.domain.excpetions;

import at.backend.MarketingCompany.customer.domain.valueobject.CustomerId;

public class CustomerNotFoundException extends RuntimeException {
  public CustomerNotFoundException(CustomerId id) {
    super("Customer not found with ID: " + id.value());
  }
}
