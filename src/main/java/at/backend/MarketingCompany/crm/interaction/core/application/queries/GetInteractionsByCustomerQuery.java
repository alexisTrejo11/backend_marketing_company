package at.backend.MarketingCompany.crm.interaction.core.application.queries;

import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import org.springframework.data.domain.Pageable;

public record GetInteractionsByCustomerQuery(CustomerCompanyId customerCompanyId, Pageable pageable) {
  public static GetInteractionsByCustomerQuery from(String customerId, Pageable pageable) {
    return new GetInteractionsByCustomerQuery(new CustomerCompanyId(customerId), pageable);
  }
}
