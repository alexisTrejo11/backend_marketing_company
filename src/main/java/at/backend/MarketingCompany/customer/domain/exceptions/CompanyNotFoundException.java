package at.backend.MarketingCompany.customer.domain.exceptions;

import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.shared.exception.NotFoundException;

public class CompanyNotFoundException extends NotFoundException {
  public CompanyNotFoundException(CustomerCompanyId id) {
    super("Company", id.value());
  }
}
