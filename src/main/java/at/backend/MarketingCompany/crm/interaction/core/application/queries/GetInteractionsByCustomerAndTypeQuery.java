package at.backend.MarketingCompany.crm.interaction.core.application.queries;

import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.InteractionType;

import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;
import org.springframework.data.domain.Pageable;

public record GetInteractionsByCustomerAndTypeQuery(
    CustomerCompanyId customerCompanyId,
    InteractionType type,
    Pageable pageable) {
}
