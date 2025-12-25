package at.backend.MarketingCompany.marketing.attribution.core.application.dto;

import lombok.Builder;
import java.math.BigDecimal;
import java.util.Map;

@Builder
public record AttributionStatistics(
    Long campaignId,
    String campaignName,
    BigDecimal totalAttributedRevenue,
    BigDecimal averageAttributionPercentage,
    Long totalAttributions,
    Long uniqueDealsAttributed,
    AttributionModelBreakdown modelBreakdown,
    TouchpointAnalysis touchpointAnalysis,
    RevenueDistribution revenueDistribution
) {
  @Builder
  public record AttributionModelBreakdown(
      Long firstTouch,
      Long lastTouch,
      Long linear,
      Long timeDecay,
      Long custom
  ) {}

  @Builder
  public record TouchpointAnalysis(
      Long totalTouchpoints,
      Double averageTouchpointsPerDeal,
      Long minTouchpoints,
      Long maxTouchpoints,
      Map<Integer, Long> touchpointDistribution
  ) {}

  @Builder
  public record RevenueDistribution(
      BigDecimal minAttributedRevenue,
      BigDecimal maxAttributedRevenue,
      BigDecimal medianAttributedRevenue,
      Map<String, BigDecimal> revenueByModel
  ) {}
}
