package at.backend.MarketingCompany.marketing.metric.core.domain.entity;

import at.backend.MarketingCompany.shared.exception.BusinessRuleException;
import java.math.BigDecimal;

public class MetricValidator {

  public static void validateForCreation(
      String name,
      BigDecimal targetValue,
      Object campaign) {
    
    if (name == null || name.isBlank()) {
      throw new BusinessRuleException("Metric name is required");
    }
    
    if (name.length() > 100) {
      throw new BusinessRuleException("Metric name cannot exceed 100 characters");
    }
    
    if (targetValue != null && targetValue.compareTo(BigDecimal.ZERO) <= 0) {
      throw new BusinessRuleException("Target value must be greater than zero");
    }
    
    if (campaign == null) {
      throw new BusinessRuleException("Campaign is required for metric");
    }
  }

  public static void validateValueUpdate(
      CampaignMetric metric,
      BigDecimal newValue) {
    
    if (metric == null) {
      throw new BusinessRuleException("Metric cannot be null");
    }
    
    if (newValue == null || newValue.compareTo(BigDecimal.ZERO) < 0) {
      throw new BusinessRuleException("Metric value cannot be negative");
    }
  }

  public static void validateForUpdate(CampaignMetric metric) {
    if (metric == null) {
      throw new BusinessRuleException("Metric cannot be null");
    }
  }
}
