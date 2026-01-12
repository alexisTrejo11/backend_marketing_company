package at.backend.MarketingCompany.marketing.metric.core.application.dto;

import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record MetricPerformanceSummary(
    Long metricId,
    String metricName,
    String metricType,
    BigDecimal currentValue,
    BigDecimal targetValue,
    BigDecimal achievementPercentage,
    Boolean isTargetAchieved,
    String measurementUnit,
    LocalDateTime lastCalculatedDate,
    String performanceLevel  // EXCELLENT, GOOD, FAIR, POOR
) {}