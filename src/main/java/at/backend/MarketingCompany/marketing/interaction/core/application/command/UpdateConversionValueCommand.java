package at.backend.MarketingCompany.marketing.interaction.core.application.command;

import java.math.BigDecimal;

import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.CampaignInteractionId;

public record UpdateConversionValueCommand(
    CampaignInteractionId interactionId,
    BigDecimal newValue,
    String reason,
    String updatedBy) {
}
