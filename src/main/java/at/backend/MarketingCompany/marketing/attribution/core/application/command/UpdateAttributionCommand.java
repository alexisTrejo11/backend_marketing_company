package at.backend.MarketingCompany.marketing.attribution.core.application.command;

import at.backend.MarketingCompany.marketing.attribution.core.domain.valueobject.CampaignAttributionId;

import java.math.BigDecimal;

public record UpdateAttributionCommand(
    CampaignAttributionId attributionId,
    BigDecimal attributionPercentage,
    BigDecimal attributedRevenue
) {}