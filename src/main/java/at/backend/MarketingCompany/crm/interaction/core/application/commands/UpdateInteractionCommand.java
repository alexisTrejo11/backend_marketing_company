package at.backend.MarketingCompany.crm.interaction.core.application.commands;

import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.InteractionDateTime;
import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.InteractionDescription;
import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.InteractionOutcome;
import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.InteractionType;

public record UpdateInteractionCommand(
    String interactionId,
    InteractionType type,
    InteractionDateTime dateTime,
    InteractionDescription description,
    InteractionOutcome outcome) {
}
