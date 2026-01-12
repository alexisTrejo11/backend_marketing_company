package at.backend.MarketingCompany.marketing.ab_test.core.application.service;

import at.backend.MarketingCompany.marketing.ab_test.core.application.statistics.AbTestStatistics;
import at.backend.MarketingCompany.marketing.ab_test.core.domain.AbTest;
import at.backend.MarketingCompany.marketing.ab_test.core.port.input.AbTestStatisticsServicePort;
import at.backend.MarketingCompany.marketing.ab_test.core.port.output.AbTestRepositoryPort;
import at.backend.MarketingCompany.marketing.campaign.core.domain.models.MarketingCampaign;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.ports.output.CampaignRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AbTestStatisticsService implements AbTestStatisticsServicePort {
	private final AbTestRepositoryPort abTestRepository;
	private final CampaignRepositoryPort campaignRepository;


	@Override
	@Transactional(readOnly = true)
	public AbTestStatistics getAbTestStatistics(MarketingCampaignId campaignId) {
		log.debug("Getting AB test statistics for campaign: {}", campaignId);

		Long totalTests = abTestRepository.countByCampaignId(campaignId);
		Long completedTests = abTestRepository.countCompletedTestsByCampaignId(campaignId);
		Long runningTests = abTestRepository.countRunningTestsByCampaignId(campaignId);
		Long scheduledTests = totalTests - completedTests - runningTests;
		BigDecimal avgSignificance = abTestRepository.calculateAverageSignificanceByCampaignId(campaignId);

		Double completionRate = calculateCompletionRate(totalTests, completedTests);

		AbTestStatistics.TestTypeBreakdown typeBreakdown = buildTypeBreakdown(campaignId);
		AbTestStatistics.ConfidenceLevelDistribution confidenceDistribution =
				buildConfidenceDistribution(campaignId);
		AbTestStatistics.TestDurationMetrics durationMetrics =
				buildDurationMetrics(campaignId);

		MarketingCampaign campaign = campaignRepository.findById(campaignId).orElse(null);

		return AbTestStatistics.builder()
				.campaignId(campaignId.getValue())
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