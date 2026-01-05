package at.backend.MarketingCompany.marketing.interaction.core.application.command;

import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.CampaignInteractionId;

public record UpdatePageInfoCommand(
    CampaignInteractionId interactionId,
    String landingPageUrl,
    String referrerUrl,
    String updatedBy) {
}
