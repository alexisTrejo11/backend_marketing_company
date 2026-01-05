package at.backend.MarketingCompany.marketing.interaction.core.application.command;

import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.CampaignInteractionId;

public record UpdateInteractionPropertyCommand(
    CampaignInteractionId interactionId,
    String key,
    Object value,
    String updatedBy,
    boolean overwrite // true for update, false for add if not exists (fails if exists)
) {
}
