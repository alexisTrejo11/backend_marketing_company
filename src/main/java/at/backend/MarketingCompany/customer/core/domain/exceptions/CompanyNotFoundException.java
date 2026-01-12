package at.backend.MarketingCompany.customer.core.domain.exceptions;

import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.shared.exception.NotFoundException;

public class CompanyNotFoundException extends NotFoundException {
  public CompanyNotFoundException(CustomerCompanyId id) {
    super("Company", id.asString());
  }
}
