package at.backend.MarketingCompany.marketing.attribution.core.application.command;

import at.backend.MarketingCompany.marketing.attribution.core.domain.valueobject.AttributionModel;
import at.backend.MarketingCompany.marketing.attribution.core.domain.valueobject.CampaignAttributionId;

public record RecalculateAttributionCommand(
    CampaignAttributionId attributionId,
    AttributionModel newModel
) {}