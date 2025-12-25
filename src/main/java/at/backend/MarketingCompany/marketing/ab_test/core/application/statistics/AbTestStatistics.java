package at.backend.MarketingCompany.marketing.ab_test.core.application.statistics;

import lombok.Builder;
import java.math.BigDecimal;

@Builder
public record AbTestStatistics(
    Long campaignId,
    String campaignName,
    Long totalTests,
    Long completedTests,
    Long runningTests,
    Long scheduledTests,
    BigDecimal averageSignificance,
    Double completionRate,
    TestTypeBreakdown typeBreakdown,
    ConfidenceLevelDistribution confidenceDistribution,
    TestDurationMetrics durationMetrics
) {
  @Builder
  public record TestTypeBreakdown(
      Long splitUrl,
      Long multivariate,
      Long bandit
  ) {}

  @Builder
  public record ConfidenceLevelDistribution(
      Long high,      // >= 0.95
      Long medium,    // 0.90-0.94
      Long low        // < 0.90
  ) {}

  @Builder
  public record TestDurationMetrics(
      Long averageDurationDays,
      Long shortestDurationDays,
      Long longestDurationDays
  ) {}
}
