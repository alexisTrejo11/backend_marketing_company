package at.backend.MarketingCompany.marketing.interaction.core.application.command;

import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.CampaignInteractionId;

public record RemoveInteractionPropertyCommand(
    CampaignInteractionId interactionId,
    String key,
    String updatedBy) {
}
