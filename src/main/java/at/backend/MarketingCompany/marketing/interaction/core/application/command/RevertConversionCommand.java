package at.backend.MarketingCompany.marketing.interaction.core.application.command;

import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.CampaignInteractionId;

public record RevertConversionCommand(
    CampaignInteractionId interactionId,
    String reason,
    String revertedBy) {
}
