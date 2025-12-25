package at.backend.MarketingCompany.marketing.metric.core.application.service;

import at.backend.MarketingCompany.marketing.campaign.core.domain.exception.MarketingDomainException;
import at.backend.MarketingCompany.marketing.campaign.core.domain.models.MarketingCampaign;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.ports.output.CampaignRepositoryPort;
import at.backend.MarketingCompany.marketing.metric.core.application.command.*;
import at.backend.MarketingCompany.marketing.metric.core.application.dto.*;
import at.backend.MarketingCompany.marketing.metric.core.application.query.MetricQuery;
import at.backend.MarketingCompany.marketing.metric.core.domain.entity.CampaignMetric;
import at.backend.MarketingCompany.marketing.metric.core.domain.entity.MetricValidator;
import at.backend.MarketingCompany.marketing.metric.core.domain.valueobject.CampaignMetricId;
import at.backend.MarketingCompany.marketing.metric.core.port.input.CampaignMetricServicePort;
import at.backend.MarketingCompany.marketing.metric.core.port.output.MetricRepositoryPort;
import at.backend.MarketingCompany.shared.exception.BusinessRuleException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CampaignMetricService implements CampaignMetricServicePort {

  private final MetricRepositoryPort metricRepository;
  private final CampaignRepositoryPort campaignRepository;

  @Override
  @Transactional
  public CampaignMetric createMetric(CreateMetricCommand command) {
    log.debug("Creating metric for campaign: {}, name: {}", 
        command.campaignId().getValue(), command.name());
    
    MarketingCampaign campaign = campaignRepository.findById(command.campaignId())
        .orElseThrow(() -> new BusinessRuleException("Campaign not found"));
    
    MetricValidator.validateForCreation(command.name(), command.targetValue(), campaign);
    
    if (metricRepository.existsByCampaignIdAndName(command.campaignId(), command.name())) {
      throw new BusinessRuleException(
          "Metric name already exists for this campaign: " + command.name()
      );
    }
    
    CampaignMetric metric = CampaignMetric.create(
        command.campaignId(),
        command.name(),
        command.metricType(),
        command.measurementUnit()
    );
    
    if (command.description() != null) {
      metric.setDescription(command.description());
    }
    if (command.targetValue() != null) {
      metric.setTargetValue(command.targetValue());
    }
    if (command.calculationFormula() != null) {
      metric.setCalculationFormula(command.calculationFormula());
    }
    if (command.dataSource() != null) {
      metric.setDataSource(command.dataSource());
    }
    if (command.isAutomated() != null && command.isAutomated()) {
      metric.setAutomated(true);
    }
    
    CampaignMetric savedMetric = metricRepository.save(metric);
    log.info("Metric created successfully with ID: {}", savedMetric.getId().getValue());
    
    return savedMetric;
  }

  @Override
  @Transactional
  public CampaignMetric updateMetric(UpdateMetricCommand command) {
    log.debug("Updating metric: {}", command.metricId().getValue());
    
    CampaignMetric metric = findMetricByIdOrThrow(command.metricId());
    MetricValidator.validateForUpdate(metric);
    
    if (command.description() != null) {
      metric.setDescription(command.description());
    }
    if (command.targetValue() != null) {
      if (command.targetValue().compareTo(BigDecimal.ZERO) <= 0) {
        throw new BusinessRuleException("Target value must be greater than zero");
      }
      metric.setTargetValue(command.targetValue());
    }
    if (command.calculationFormula() != null) {
      metric.setCalculationFormula(command.calculationFormula());
    }
    if (command.dataSource() != null) {
      metric.setDataSource(command.dataSource());
    }
    
    CampaignMetric updatedMetric = metricRepository.save(metric);
    log.info("Metric updated successfully: {}", command.metricId().getValue());
    
    return updatedMetric;
  }

  @Override
  @Transactional
  public CampaignMetric updateMetricValue(UpdateMetricValueCommand command) {
    log.debug("Updating metric value: {} to {}", 
        command.metricId().getValue(), command.newValue());
    
    CampaignMetric metric = findMetricByIdOrThrow(command.metricId());
    MetricValidator.validateValueUpdate(metric, command.newValue());
    
    metric.updateValue(command.newValue());
    
    CampaignMetric updatedMetric = metricRepository.save(metric);
    log.info("Metric value updated successfully: {}", command.metricId().getValue());
    
    return updatedMetric;
  }

  @Override
  @Transactional
  public CampaignMetric markAsAutomated(CampaignMetricId metricId) {
    log.debug("Marking metric as automated: {}", metricId.getValue());
    
    CampaignMetric metric = findMetricByIdOrThrow(metricId);
    metric.setAutomated(true);
    
    CampaignMetric updatedMetric = metricRepository.save(metric);
    log.info("Metric marked as automated: {}", metricId.getValue());
    
    return updatedMetric;
  }

  @Override
  @Transactional
  public void deleteMetric(CampaignMetricId metricId) {
    log.debug("Deleting metric: {}", metricId.getValue());
    
    CampaignMetric metric = findMetricByIdOrThrow(metricId);
    metric.softDelete();
    metricRepository.save(metric);
    
    log.info("Metric deleted successfully: {}", metricId.getValue());
  }

  @Override
  @Transactional(readOnly = true)
  public CampaignMetric getMetricById(CampaignMetricId metricId) {
    return findMetricByIdOrThrow(metricId);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CampaignMetric> searchMetrics(MetricQuery query, Pageable pageable) {
    if (query.isEmpty()) {
      return metricRepository.findAll(pageable);
    }
    
    return metricRepository.findByFilters(query, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CampaignMetric> getMetricsByCampaign(MarketingCampaignId campaignId, Pageable pageable) {
    return metricRepository.findByCampaignId(campaignId, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CampaignMetric> getAchievedMetrics(MarketingCampaignId campaignId, Pageable pageable) {
    return metricRepository.findByCampaignIdAndIsTargetAchieved(campaignId, true, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CampaignMetric> getNotAchievedMetrics(MarketingCampaignId campaignId, Pageable pageable) {
    return metricRepository.findByCampaignIdAndIsTargetAchieved(campaignId, false, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CampaignMetric> getAutomatedMetrics(MarketingCampaignId campaignId, Pageable pageable) {
    return metricRepository.findByCampaignIdAndIsAutomated(campaignId, true, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CampaignMetric> getRecentlyUpdatedMetrics(
      LocalDateTime fromDate, 
      Pageable pageable) {
    return metricRepository.findRecentlyUpdated(fromDate, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public MetricStatistics getMetricStatistics(MarketingCampaignId campaignId) {
    log.debug("Getting metric statistics for campaign: {}", campaignId);
    
    Long totalMetrics = metricRepository.countByCampaignId(campaignId);
    Long achievedMetrics = metricRepository.countAchievedMetricsByCampaignId(campaignId);
    Long notAchievedMetrics = totalMetrics - achievedMetrics;
    Long automatedMetrics = metricRepository.countAutomatedMetricsByCampaignId(campaignId);
    Long manualMetrics = totalMetrics - automatedMetrics;
    
    Double achievementRate = calculateAchievementRate(totalMetrics, achievedMetrics);
    
    MetricStatistics.MetricTypeBreakdown typeBreakdown = buildTypeBreakdown(campaignId);
    MetricStatistics.PerformanceDistribution performanceDistribution = 
        buildPerformanceDistribution(campaignId);
    
    MarketingCampaign campaign = campaignRepository.findById(campaignId).orElse(null);
    
    return MetricStatistics.builder()
        .campaignId(campaignId.getValue())
        .campaignName(campaign != null ? campaign.getName().value() : null)
        .totalMetrics(totalMetrics)
        .achievedMetrics(achievedMetrics)
        .notAchievedMetrics(notAchievedMetrics)
        .automatedMetrics(automatedMetrics)
        .manualMetrics(manualMetrics)
        .achievementRate(achievementRate)
        .typeBreakdown(typeBreakdown)
        .performanceDistribution(performanceDistribution)
        .build();
  }

  @Override
  @Transactional(readOnly = true)
  public MetricPerformanceSummary getMetricPerformance(CampaignMetricId metricId) {
    log.debug("Getting metric performance for: {}", metricId.getValue());
    
    CampaignMetric metric = findMetricByIdOrThrow(metricId);
    
    BigDecimal achievementPercentage = metric.achievementPercentage();
    String performanceLevel = determinePerformanceLevel(achievementPercentage);
    
    return MetricPerformanceSummary.builder()
        .metricId(metric.getId().getValue())
        .metricName(metric.getName())
        .metricType(metric.getMetricType() != null ? metric.getMetricType().name() : null)
        .currentValue(metric.getCurrentValue())
        .targetValue(metric.getTargetValue())
        .achievementPercentage(achievementPercentage)
        .isTargetAchieved(metric.isTargetAchieved())
        .measurementUnit(metric.getMeasurementUnit())
        .lastCalculatedDate(metric.getLastCalculatedDate())
        .performanceLevel(performanceLevel)
        .build();
  }

  @Override
  @Transactional(readOnly = true)
  public Double getAchievementRate(MarketingCampaignId campaignId) {
    Long totalMetrics = metricRepository.countByCampaignId(campaignId);
    Long achievedMetrics = metricRepository.countAchievedMetricsByCampaignId(campaignId);
    
    return calculateAchievementRate(totalMetrics, achievedMetrics);
  }


  private CampaignMetric findMetricByIdOrThrow(CampaignMetricId metricId) {
    return metricRepository.findById(metricId)
        .orElseThrow(() -> new MarketingDomainException(
            "Metric not found with id: " + metricId.getValue()
        ));
  }

  private Double calculateAchievementRate(Long total, Long achieved) {
    if (total == null || total == 0) {
      return 0.0;
    }
    return BigDecimal.valueOf(achieved)
        .divide(BigDecimal.valueOf(total), 4, RoundingMode.HALF_UP)
        .multiply(BigDecimal.valueOf(100))
        .doubleValue();
  }

  private MetricStatistics.MetricTypeBreakdown buildTypeBreakdown(MarketingCampaignId campaignId) {
    Map<String, Long> typeCounts = metricRepository.countByMetricTypeByCampaignId(campaignId);
    
    return MetricStatistics.MetricTypeBreakdown.builder()
        .countMetrics(typeCounts.getOrDefault("COUNT", 0L))
        .currencyMetrics(typeCounts.getOrDefault("CURRENCY", 0L))
        .percentageMetrics(typeCounts.getOrDefault("PERCENTAGE", 0L))
        .durationMetrics(typeCounts.getOrDefault("DURATION", 0L))
        .costMetrics(typeCounts.getOrDefault("COST", 0L))
        .ratioMetrics(typeCounts.getOrDefault("RATIO", 0L))
        .scoreMetrics(typeCounts.getOrDefault("SCORE", 0L))
        .build();
  }

  private MetricStatistics.PerformanceDistribution buildPerformanceDistribution(MarketingCampaignId campaignId) {
    Map<String, Long> distribution = metricRepository.getPerformanceDistributionByCampaignId(campaignId);
    BigDecimal avgAchievement = metricRepository.calculateAverageAchievementByCampaignId(campaignId);
    
    return MetricStatistics.PerformanceDistribution.builder()
        .exceededTarget(distribution.getOrDefault("EXCEEDED", 0L))
        .metTarget(distribution.getOrDefault("MET", 0L))
        .nearTarget(distribution.getOrDefault("NEAR", 0L))
        .belowTarget(distribution.getOrDefault("BELOW", 0L))
        .farBelowTarget(distribution.getOrDefault("FAR_BELOW", 0L))
        .averageAchievement(avgAchievement)
        .build();
  }

  private String determinePerformanceLevel(BigDecimal achievementPercentage) {
    if (achievementPercentage == null) {
      return "UNKNOWN";
    }
    
    if (achievementPercentage.compareTo(BigDecimal.valueOf(100)) >= 0) {
      return "EXCELLENT";
    } else if (achievementPercentage.compareTo(BigDecimal.valueOf(80)) >= 0) {
      return "GOOD";
    } else if (achievementPercentage.compareTo(BigDecimal.valueOf(50)) >= 0) {
      return "FAIR";
    } else {
      return "POOR";
    }
  }
}