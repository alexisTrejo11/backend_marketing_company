package at.backend.MarketingCompany.marketing.interaction.core.application.command;

import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.CampaignInteractionId;

public record UpdateUTMParametersCommand(
    CampaignInteractionId interactionId,
    String source,
    String medium,
    String campaign,
    String content,
    String term,
    String updatedBy) {
}
