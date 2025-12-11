package at.backend.MarketingCompany.crm.interaction.application.queries;

import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.InteractionType;

import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;
import org.springframework.data.domain.Pageable;

public record GetInteractionsByCustomerAndTypeQuery(
    CustomerCompanyId customerCompanyId,
    InteractionType type,
    Pageable pageable) {
}
