package at.backend.MarketingCompany.crm.interaction.application.queries;

import at.backend.MarketingCompany.customer.domain.ValueObjects.CustomerId;
import org.springframework.data.domain.Pageable;

public record GetInteractionsByCustomerQuery(CustomerId customerId, Pageable pageable) {
    public static GetInteractionsByCustomerQuery from(String customerId, Pageable pageable) {
        return new GetInteractionsByCustomerQuery(new CustomerId(customerId), pageable);
    }
}
