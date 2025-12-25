package at.backend.MarketingCompany.marketing.metric.core.application.query;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MetricType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record MetricQuery(
    Long campaignId,
    List<MetricType> metricTypes,
    Boolean isTargetAchieved,
    Boolean isAutomated,
    BigDecimal minCurrentValue,
    BigDecimal maxCurrentValue,
    LocalDateTime lastCalculatedAfter,
    String searchTerm
) {
  public static MetricQuery empty() {
    return new MetricQuery(null, null, null, null, null, null, null, null);
  }

  public boolean isEmpty() {
    return campaignId == null &&
           (metricTypes == null || metricTypes.isEmpty()) &&
           isTargetAchieved == null &&
           isAutomated == null &&
           minCurrentValue == null &&
           maxCurrentValue == null &&
           lastCalculatedAfter == null &&
           (searchTerm == null || searchTerm.isBlank());
  }
}