package at.backend.MarketingCompany.marketing.attribution.core.application.command;

import at.backend.MarketingCompany.marketing.attribution.core.domain.valueobject.CampaignAttributionId;

import java.time.LocalDateTime;

public record AddTouchpointCommand(
    CampaignAttributionId attributionId,
    LocalDateTime touchTimestamp
) {}