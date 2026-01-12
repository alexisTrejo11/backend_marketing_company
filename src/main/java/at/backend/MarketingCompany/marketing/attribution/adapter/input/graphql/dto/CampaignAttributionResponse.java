package at.backend.MarketingCompany.marketing.attribution.adapter.input.graphql.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CampaignAttributionResponse(
    Long id,
    Long dealId,
    Long campaignId,
    String attributionModel,
    BigDecimal attributionPercentage,
    BigDecimal attributedRevenue,
    List<LocalDateTime> touchTimestamps,
    Integer touchCount,
    BigDecimal firstTouchWeight,
    BigDecimal lastTouchWeight,
    BigDecimal linearWeight,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}