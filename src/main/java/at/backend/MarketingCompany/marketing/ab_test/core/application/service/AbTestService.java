package at.backend.MarketingCompany.marketing.ab_test.core.application.service;

import at.backend.MarketingCompany.marketing.ab_test.core.application.command.CompleteAbTestCommand;
import at.backend.MarketingCompany.marketing.ab_test.core.application.command.CreateAbTestCommand;
import at.backend.MarketingCompany.marketing.ab_test.core.application.command.UpdateAbTestCommand;
import at.backend.MarketingCompany.marketing.ab_test.core.application.query.AbTestQuery;
import at.backend.MarketingCompany.marketing.ab_test.core.application.statistics.AbTestStatistics;
import at.backend.MarketingCompany.marketing.ab_test.core.domain.AbTest;
import at.backend.MarketingCompany.marketing.ab_test.core.domain.AbTestValidator;
import at.backend.MarketingCompany.marketing.ab_test.core.domain.valueobject.AbTestId;
import at.backend.MarketingCompany.marketing.ab_test.core.port.input.AbTestServicePort;
import at.backend.MarketingCompany.marketing.ab_test.core.port.output.AbTestRepositoryPort;
import at.backend.MarketingCompany.marketing.campaign.core.domain.exception.MarketingDomainException;
import at.backend.MarketingCompany.marketing.campaign.core.domain.models.MarketingCampaign;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.ports.output.CampaignRepositoryPort;
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
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AbTestService implements AbTestServicePort {
  private final AbTestRepositoryPort abTestRepository;
  private final CampaignRepositoryPort campaignRepository;

  @Override
  @Transactional
  public AbTest createAbTest(CreateAbTestCommand command) {
    log.debug("Creating AB test for campaign: {}, name: {}", 
        command.campaignId().getValue(), command.testName());
    
    MarketingCampaign campaign = campaignRepository.findById(command.campaignId())
        .orElseThrow(() -> new BusinessRuleException("Campaign not found"));
    
    AbTestValidator.validateForCreation(
        command.testName(),
        command.startDate(),
        command.controlVariant(),
        command.treatmentVariants(),
        campaign
    );
    
    if (command.requiredSampleSize() != null) {
      AbTestValidator.validateSampleSize(command.requiredSampleSize());
    }
    
    if (command.confidenceLevel() != null) {
      AbTestValidator.validateConfidenceLevel(command.confidenceLevel());
    }
    
    if (abTestRepository.existsByNameAndCampaignId(
        command.testName(), 
        command.campaignId())) {
      throw new BusinessRuleException(
          "AB Test name already exists for this campaign: " + command.testName()
      );
    }
    
    AbTest test = AbTest.create(
        command.campaignId(),
        command.testName(),
        command.testType(),
        command.primaryMetric(),
        command.controlVariant(),
        command.treatmentVariants(),
        command.startDate()
    );
    
    if (command.hypothesis() != null) {
      test.setHypothesis(command.hypothesis());
    }
    if (command.confidenceLevel() != null) {
      test.setConfidenceLevel(command.confidenceLevel());
    }
    if (command.requiredSampleSize() != null) {
      test.setRequiredSampleSize(command.requiredSampleSize());
    }
    
    AbTest savedTest = abTestRepository.save(test);
    log.info("AB test created successfully with ID: {}", savedTest.getId().getValue());
    
    return savedTest;
  }

  @Override
  @Transactional
  public AbTest updateAbTest(UpdateAbTestCommand command) {
    log.debug("Updating AB test: {}", command.testId());
    
    AbTest test = findAbTestByIdOrThrow(command.testId());
    AbTestValidator.validateForUpdate(test);
    
    if (command.hypothesis() != null) {
      test.setHypothesis(command.hypothesis());
    }
    if (command.confidenceLevel() != null) {
      AbTestValidator.validateConfidenceLevel(command.confidenceLevel());
      test.setConfidenceLevel(command.confidenceLevel());
    }
    if (command.requiredSampleSize() != null) {
      AbTestValidator.validateSampleSize(command.requiredSampleSize());
      test.setRequiredSampleSize(command.requiredSampleSize());
    }
    if (command.endDate() != null) {
      test.setEndDate(command.endDate());
    }
    
    AbTest updatedTest = abTestRepository.save(test);
    log.info("AB test updated successfully: {}", command.testId().getValue());
    
    return updatedTest;
  }

  @Override
  @Transactional
  public AbTest completeAbTest(CompleteAbTestCommand command) {
    log.debug("Completing AB test: {}", command.testId().getValue());
    
    AbTest test = findAbTestByIdOrThrow(command.testId());
    
    AbTestValidator.validateCompletion(
        test,
        command.winningVariant(),
        command.statisticalSignificance()
    );
    
    test.complete(command.winningVariant(), command.statisticalSignificance());
    
    AbTest completedTest = abTestRepository.save(test);
    log.info("AB test completed successfully: {}", command.testId().getValue());
    
    return completedTest;
  }

  @Override
  @Transactional
  public void deleteAbTest(AbTestId testId) {
    log.debug("Deleting AB test: {}", testId.getValue());
    
    AbTest test = findAbTestByIdOrThrow(testId);
    
    if (test.hasStarted() && !test.isCompleted()) {
      throw new BusinessRuleException(
          "Cannot delete a running AB test. Please complete it first."
      );
    }
    
    test.softDelete();
    abTestRepository.save(test);
    
    log.info("AB test deleted successfully: {}", testId.getValue());
  }

  @Override
  @Transactional(readOnly = true)
  public AbTest getAbTestById(AbTestId testId) {
    return findAbTestByIdOrThrow(testId);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<AbTest> searchAbTests(AbTestQuery query, Pageable pageable) {
    if (query.isEmpty()) {
      return abTestRepository.findAll(pageable);
    }
    
    return abTestRepository.findByFilters(query, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<AbTest> getAbTestsByCampaign(MarketingCampaignId campaignId, Pageable pageable) {
    return abTestRepository.findByCampaignId(campaignId, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<AbTest> getCompletedTests(Pageable pageable) {
    return abTestRepository.findByCompletionStatus(true, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<AbTest> getRunningTests(Pageable pageable) {
    return abTestRepository.findRunningTests(pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<AbTest> getScheduledTests(Pageable pageable) {
    return abTestRepository.findScheduledTests(pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public AbTestStatistics getAbTestStatistics(MarketingCampaignId campaignId) {
    log.debug("Getting AB test statistics for campaign: {}", campaignId);
    
    Long totalTests = abTestRepository.countByCampaignId(campaignId);
    Long completedTests = abTestRepository.countCompletedTestsByCampaignId(campaignId);
    Long runningTests = abTestRepository.countRunningTestsByCampaignId(campaignId);
    Long scheduledTests = totalTests - completedTests - runningTests;
    
    BigDecimal avgSignificance = abTestRepository
        .calculateAverageSignificanceByCampaignId(campaignId);
    
    Double completionRate = calculateCompletionRate(totalTests, completedTests);
    
    AbTestStatistics.TestTypeBreakdown typeBreakdown = buildTypeBreakdown(campaignId);
    AbTestStatistics.ConfidenceLevelDistribution confidenceDistribution = 
        buildConfidenceDistribution(campaignId);
    AbTestStatistics.TestDurationMetrics durationMetrics = 
        buildDurationMetrics(campaignId);
    
    MarketingCampaign campaign = campaignRepository.findById(campaignId).orElse(null);
    
    return AbTestStatistics.builder()
        .campaignId(campaignId.getValue())
        .campaignName(campaign != null ? campaign.getName().value() : null)
        .totalTests(totalTests)
        .completedTests(completedTests)
        .runningTests(runningTests)
        .scheduledTests(scheduledTests)
        .averageSignificance(avgSignificance)
        .completionRate(completionRate)
        .typeBreakdown(typeBreakdown)
        .confidenceDistribution(confidenceDistribution)
        .durationMetrics(durationMetrics)
        .build();
  }

  @Override
  @Transactional(readOnly = true)
  public Double getCompletionRate(MarketingCampaignId campaignId) {
    Long totalTests = abTestRepository.countByCampaignId(campaignId);
    Long completedTests = abTestRepository.countCompletedTestsByCampaignId(campaignId);
    
    return calculateCompletionRate(totalTests, completedTests);
  }

  @Override
  @Transactional(readOnly = true)
  public BigDecimal getAverageSignificance(MarketingCampaignId campaignId) {
    return abTestRepository.calculateAverageSignificanceByCampaignId(campaignId);
  }

  // ========== PRIVATE HELPER METHODS ==========

  private AbTest findAbTestByIdOrThrow(AbTestId testId) {
    return abTestRepository.findById(testId)
        .orElseThrow(() -> new MarketingDomainException(
            "AB Test not found with id: " + testId.getValue()
        ));
  }

  private Double calculateCompletionRate(Long total, Long completed) {
    if (total == null || total == 0) {
      return 0.0;
    }
    return BigDecimal.valueOf(completed)
        .divide(BigDecimal.valueOf(total), 4, RoundingMode.HALF_UP)
        .multiply(BigDecimal.valueOf(100))
        .doubleValue();
  }

  private AbTestStatistics.TestTypeBreakdown buildTypeBreakdown(MarketingCampaignId campaignId) {
    Map<String, Long> typeCounts = abTestRepository
        .countByTestTypeByCampaignId(campaignId);
    
    return AbTestStatistics.TestTypeBreakdown.builder()
        .splitUrl(typeCounts.getOrDefault("SPLIT_URL", 0L))
        .multivariate(typeCounts.getOrDefault("MULTIVARIATE", 0L))
        .bandit(typeCounts.getOrDefault("BANDIT", 0L))
        .build();
  }

  private AbTestStatistics.ConfidenceLevelDistribution buildConfidenceDistribution(MarketingCampaignId campaignId) {
    List<BigDecimal> confidenceLevels = abTestRepository
        .getAllConfidenceLevelsByCampaignId(campaignId);
    
    long high = confidenceLevels.stream()
        .filter(cl -> cl.compareTo(new BigDecimal("0.95")) >= 0)
        .count();
    long medium = confidenceLevels.stream()
        .filter(cl -> cl.compareTo(new BigDecimal("0.90")) >= 0 && 
                     cl.compareTo(new BigDecimal("0.95")) < 0)
        .count();
    long low = confidenceLevels.stream()
        .filter(cl -> cl.compareTo(new BigDecimal("0.90")) < 0)
        .count();
    
    return AbTestStatistics.ConfidenceLevelDistribution.builder()
        .high(high)
        .medium(medium)
        .low(low)
        .build();
  }

  private AbTestStatistics.TestDurationMetrics buildDurationMetrics(MarketingCampaignId campaignId) {
    List<AbTest> completedTests = abTestRepository
        .findCompletedTestsByCampaignId(campaignId);
    
    if (completedTests.isEmpty()) {
      return AbTestStatistics.TestDurationMetrics.builder()
          .averageDurationDays(0L)
          .shortestDurationDays(0L)
          .longestDurationDays(0L)
          .build();
    }
    
    List<Long> durations = completedTests.stream()
        .filter(test -> test.getStartDate() != null && test.getEndDate() != null)
        .map(test -> ChronoUnit.DAYS.between(test.getStartDate(), test.getEndDate()))
        .toList();
    
    if (durations.isEmpty()) {
      return AbTestStatistics.TestDurationMetrics.builder()
          .averageDurationDays(0L)
          .shortestDurationDays(0L)
          .longestDurationDays(0L)
          .build();
    }
    
    long avgDuration = (long) durations.stream()
        .mapToLong(Long::longValue)
        .average()
        .orElse(0.0);
    long minDuration = durations.stream()
        .min(Long::compareTo)
        .orElse(0L);
    long maxDuration = durations.stream()
        .max(Long::compareTo)
        .orElse(0L);
    
    return AbTestStatistics.TestDurationMetrics.builder()
        .averageDurationDays(avgDuration)
        .shortestDurationDays(minDuration)
        .longestDurationDays(maxDuration)
        .build();
  }
}