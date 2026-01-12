package at.backend.MarketingCompany.marketing.ab_test.core.application.statistics;

import lombok.Builder;
import java.math.BigDecimal;

@Builder
public record AbTestStatistics(
    Long campaignId,
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

  public AbTestStatistics {
    if (totalTests == 0) {
      averageSignificance = BigDecimal.ZERO;
      completionRate = 0.0;
    }

    if (typeBreakdown == null) {
      typeBreakdown = TestTypeBreakdown.builder()
          .splitUrl(0L)
          .multivariate(0L)
          .bandit(0L)
          .build();
    }

    if  (confidenceDistribution == null) {
      confidenceDistribution = ConfidenceLevelDistribution.builder()
          .high(0L)
          .medium(0L)
          .low(0L)
          .build();
    }

    if (durationMetrics == null) {
      durationMetrics = TestDurationMetrics.builder()
          .averageDurationDays(0L)
          .shortestDurationDays(0L)
          .longestDurationDays(0L)
          .build();
    }

    if (completionRate != null && completionRate > 1.0) {
      completionRate = 1.0;
    }

    if (completionRate != null && completionRate < 0.0) {
      completionRate = 0.0;
    }

    if (averageSignificance != null) {
      if (averageSignificance.compareTo(BigDecimal.ZERO) < 0) {
        averageSignificance = BigDecimal.ZERO;
      } else if (averageSignificance.compareTo(BigDecimal.ONE) > 0) {
        averageSignificance = BigDecimal.ONE;
      }
    }

    if (completedTests != null && totalTests > 0) {
      completionRate = completedTests.doubleValue() / totalTests.doubleValue();
    } else {
      completionRate = 0.0;
    }

    if (averageSignificance == null) {
      averageSignificance = BigDecimal.ZERO;
    }
  }

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
