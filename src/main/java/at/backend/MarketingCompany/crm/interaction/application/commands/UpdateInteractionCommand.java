package at.backend.MarketingCompany.crm.interaction.application.commands;

import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.InteractionDateTime;
import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.InteractionDescription;
import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.InteractionOutcome;
import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.InteractionType;

public record UpdateInteractionCommand(
    String interactionId,
    InteractionType type,
    InteractionDateTime dateTime,
    InteractionDescription description,
    InteractionOutcome outcome
) {}
