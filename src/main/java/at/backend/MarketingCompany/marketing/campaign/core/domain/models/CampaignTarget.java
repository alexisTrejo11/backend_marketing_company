package at.backend.MarketingCompany.marketing.campaign.core.domain.models;

import java.math.BigDecimal;

public record CampaignTarget(
    String metricName,
    MetricType metricType,
    BigDecimal targetValue,
    BigDecimal currentValue,
    String measurementUnit
) {
  public CampaignTarget {
    if (metricName == null || metricName.isBlank()) {
      throw new IllegalArgumentException("Metric name is required");
    }
    if (metricType == null) {
      throw new IllegalArgumentException("Metric type is required");
    }
    if (targetValue == null || targetValue.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Target value must be positive");
    }
    if (currentValue == null) {
      currentValue = BigDecimal.ZERO;
    }
  }

  public boolean isAchieved() {
    return currentValue.compareTo(targetValue) >= 0;
  }

  public BigDecimal achievementPercentage() {
    if (targetValue.compareTo(BigDecimal.ZERO) == 0) {
      return BigDecimal.ZERO;
    }
    return currentValue.divide(targetValue, 4, BigDecimal.ROUND_HALF_UP)
        .multiply(BigDecimal.valueOf(100));
  }

  public enum MetricType {
    COUNT, CURRENCY, PERCENTAGE, DURATION, RATIO
  }
}