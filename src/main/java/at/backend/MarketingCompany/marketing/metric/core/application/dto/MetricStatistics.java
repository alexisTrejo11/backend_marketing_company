package at.backend.MarketingCompany.marketing.metric.core.application.dto;

import lombok.Builder;
import java.math.BigDecimal;

@Builder
public record MetricStatistics(
    Long campaignId,
    String campaignName,
    Long totalMetrics,
    Long achievedMetrics,
    Long notAchievedMetrics,
    Long automatedMetrics,
    Long manualMetrics,
    Double achievementRate,
    MetricTypeBreakdown typeBreakdown,
    PerformanceDistribution performanceDistribution
) {
  @Builder
  public record MetricTypeBreakdown(
      Long countMetrics,
      Long currencyMetrics,
      Long percentageMetrics,
      Long durationMetrics,
      Long costMetrics,
      Long ratioMetrics,
      Long scoreMetrics
  ) {}

  @Builder
  public record PerformanceDistribution(
      Long exceededTarget,      // > 100%
      Long metTarget,            // = 100%
      Long nearTarget,           // 80-99%
      Long belowTarget,          // 50-79%
      Long farBelowTarget,       // < 50%
      BigDecimal averageAchievement
  ) {}
}
