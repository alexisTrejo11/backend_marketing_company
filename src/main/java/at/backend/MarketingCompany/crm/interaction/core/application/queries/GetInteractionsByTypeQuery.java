package at.backend.MarketingCompany.crm.interaction.core.application.queries;

import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.InteractionType;
import org.springframework.data.domain.Pageable;

public record GetInteractionsByTypeQuery(InteractionType type, Pageable pageable) {
}
