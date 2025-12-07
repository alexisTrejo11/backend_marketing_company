package at.backend.MarketingCompany.crm.interaction.application.queries;

import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.InteractionType;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerId;

import org.springframework.data.domain.Pageable;

public record GetInteractionsByCustomerAndTypeQuery(
    CustomerId customerId,
    InteractionType type,
    Pageable pageable) {
}
