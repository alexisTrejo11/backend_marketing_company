package at.backend.MarketingCompany.customer.domain.exceptions;

import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;

public class CompanyNotFoundException extends RuntimeException {
  public CompanyNotFoundException(CustomerCompanyId id) {
    super("Company not found with ID: " + id.value());
  }
}
