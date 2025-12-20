package at.backend.MarketingCompany.marketing.metric.core.domain.entity;

import at.backend.MarketingCompany.marketing.campaign.core.domain.exception.MarketingDomainException;
import at.backend.MarketingCompany.marketing.campaign.core.domain.models.MetricType;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.metric.core.domain.valueobject.CampaignMetricId;
import at.backend.MarketingCompany.marketing.metric.core.domain.valueobject.MetricType;
import at.backend.MarketingCompany.shared.domain.BaseDomainEntity;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class CampaignMetric extends BaseDomainEntity<CampaignMetricId> {
  private MarketingCampaignId campaignId;
  private String name;
  private MetricType metricType;
  private String description;
  private BigDecimal currentValue;
  private BigDecimal targetValue;
  private String measurementUnit;
  private String calculationFormula;
  private String dataSource;
  private LocalDateTime lastCalculatedDate;
  private boolean isAutomated;
  private boolean isTargetAchieved;

  private CampaignMetric() {
    this.currentValue = BigDecimal.ZERO;
    this.isAutomated = false;
    this.isTargetAchieved = false;
  }

  public CampaignMetric(CampaignMetricId id) {
    super(id);
    this.currentValue = BigDecimal.ZERO;
    this.isAutomated = false;
    this.isTargetAchieved = false;
  }

  public static CampaignMetric create(
      MarketingCampaignId campaignId,
      String name,
      MetricType metricType,
      String measurementUnit) {
    
    if (campaignId == null) {
      throw new MarketingDomainException("Campaign ID is required");
    }
    if (name == null || name.isBlank()) {
      throw new MarketingDomainException("Metric name is required");
    }
    if (metricType == null) {
      throw new MarketingDomainException("Metric type is required");
    }

    CampaignMetric metric = new CampaignMetric(CampaignMetricId.generate());
    metric.campaignId = campaignId;
    metric.name = name;
    metric.metricType = metricType;
    metric.measurementUnit = measurementUnit;
    metric.currentValue = BigDecimal.ZERO;
    metric.isAutomated = false;
    metric.isTargetAchieved = false;

    return metric;
  }

  public static CampaignMetric reconstruct(CampaignMetricReconstructParams params) {
    if (params == null) {
      return null;
    }

    CampaignMetric metric = new CampaignMetric();
    metric.id = params.id();
    metric.campaignId = params.campaignId();
    metric.name = params.name();
    metric.metricType = params.metricType();
    metric.description = params.description();
    metric.currentValue = params.currentValue() != null ? params.currentValue() : BigDecimal.ZERO;
    metric.targetValue = params.targetValue();
    metric.measurementUnit = params.measurementUnit();
    metric.calculationFormula = params.calculationFormula();
    metric.dataSource = params.dataSource();
    metric.lastCalculatedDate = params.lastCalculatedDate();
    metric.isAutomated = params.isAutomated() != null ? params.isAutomated() : false;
    metric.isTargetAchieved = params.isTargetAchieved() != null ? params.isTargetAchieved() : false;
    metric.createdAt = params.createdAt();
    metric.updatedAt = params.updatedAt();
    metric.deletedAt = params.deletedAt();
    metric.version = params.version();

    return metric;
  }

  public void updateValue(BigDecimal newValue) {
    if (newValue == null || newValue.compareTo(BigDecimal.ZERO) < 0) {
      throw new MarketingDomainException("Metric value cannot be negative");
    }
    this.currentValue = newValue;
    this.lastCalculatedDate = LocalDateTime.now();
    
    if (targetValue != null) {
      this.isTargetAchieved = currentValue.compareTo(targetValue) >= 0;
    }
  }

  public BigDecimal achievementPercentage() {
    if (targetValue == null || targetValue.compareTo(BigDecimal.ZERO) == 0) {
      return BigDecimal.ZERO;
    }
    return currentValue.divide(targetValue, 4, BigDecimal.ROUND_HALF_UP)
        .multiply(BigDecimal.valueOf(100));
  }


}