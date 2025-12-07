package at.backend.MarketingCompany.crm.interaction.application.queries;

import org.springframework.data.domain.Pageable;

import at.backend.MarketingCompany.customer.domain.valueobject.CustomerId;

public record GetInteractionsByCustomerQuery(CustomerId customerId, Pageable pageable) {
  public static GetInteractionsByCustomerQuery from(String customerId, Pageable pageable) {
    return new GetInteractionsByCustomerQuery(new CustomerId(customerId), pageable);
  }
}
