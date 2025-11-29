package at.backend.MarketingCompany.crm.interaction.application.queries;

import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.InteractionType;
import org.springframework.data.domain.Pageable;

public record GetInteractionsByTypeQuery(InteractionType type, Pageable pageable) {}
