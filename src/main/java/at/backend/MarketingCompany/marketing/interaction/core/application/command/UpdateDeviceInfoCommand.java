package at.backend.MarketingCompany.marketing.interaction.core.application.command;

import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.CampaignInteractionId;

public record UpdateDeviceInfoCommand(
    CampaignInteractionId interactionId,
    String deviceType,
    String deviceOs,
    String browser,
    String updatedBy) {
}
