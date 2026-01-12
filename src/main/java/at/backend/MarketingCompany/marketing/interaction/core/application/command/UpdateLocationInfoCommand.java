package at.backend.MarketingCompany.marketing.interaction.core.application.command;

import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.CampaignInteractionId;

public record UpdateLocationInfoCommand(
    CampaignInteractionId interactionId,
    String countryCode,
    String city,
    String updatedBy) {
}
