package at.backend.MarketingCompany.customer.application.port.input;

import at.backend.MarketingCompany.customer.application.dto.command.*;
import at.backend.MarketingCompany.customer.domain.Customer;

public interface CustomerCommandHandler {
  Customer handle(CustomerCreateCommand command);

  Customer handle(CustomerUpdateCommand command);

  Customer handle(ActivateCustomerCommand command);

  Customer handle(BlockCustomerCommand command);

  Customer handle(DeleteCustomerCommand command);
}
