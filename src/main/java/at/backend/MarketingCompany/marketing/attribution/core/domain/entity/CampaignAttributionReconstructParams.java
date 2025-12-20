package at.backend.MarketingCompany.marketing.attribution.core.domain.entity;

import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.DealId;
import at.backend.MarketingCompany.marketing.attribution.core.domain.valueobject.AttributionModel;
import at.backend.MarketingCompany.marketing.attribution.core.domain.valueobject.CampaignAttributionId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CampaignAttributionReconstructParams(
    CampaignAttributionId id,
    DealId dealId,
    MarketingCampaignId campaignId,
    AttributionModel attributionModel,
    BigDecimal attributionPercentage,
    BigDecimal attributedRevenue,
    List<LocalDateTime> touchTimestamps,
    Integer touchCount,
    BigDecimal firstTouchWeight,
    BigDecimal lastTouchWeight,
    BigDecimal linearWeight,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime deletedAt,
    Integer version
) {}
