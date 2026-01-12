package at.backend.MarketingCompany.marketing.ab_test.adapter.input.graphql.mapper;

import at.backend.MarketingCompany.marketing.ab_test.adapter.input.graphql.dto.AbTestOutput;
import at.backend.MarketingCompany.marketing.ab_test.adapter.input.graphql.dto.AbTestStatisticsOutput;
import at.backend.MarketingCompany.marketing.ab_test.core.application.statistics.AbTestStatistics;
import at.backend.MarketingCompany.marketing.ab_test.core.domain.AbTest;
import at.backend.MarketingCompany.shared.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class AbTestOutputMapper {

	public AbTestOutput toOutput(AbTest abTest) {
		if (abTest == null) {
			return null;
		}

		return AbTestOutput.builder()
				.id(abTest.getId() != null ? abTest.getId().getValue() : null)
				.campaignId(abTest.getCampaignId() != null ? abTest.getCampaignId().getValue() : null)
				.testName(abTest.getTestName())
				.hypothesis(abTest.getHypothesis())
				.testType(abTest.getTestType())
				.primaryMetric(abTest.getPrimaryMetric())
				.confidenceLevel(abTest.getConfidenceLevel())
				.requiredSampleSize(abTest.getRequiredSampleSize())
				.controlVariant(abTest.getControlVariant())
				.treatmentVariants(abTest.getTreatmentVariants())
				.winningVariant(abTest.getWinningVariant())
				.statisticalSignificance(abTest.getStatisticalSignificance())
				.isCompleted(abTest.isCompleted())
				.hasStarted(abTest.hasStarted())
				.hasEnded(abTest.hasEnded())
				.startDate(abTest.getStartDate().atOffset(java.time.ZoneOffset.UTC))
				.endDate(abTest.getEndDate() != null ? abTest.getEndDate().atOffset(java.time.ZoneOffset.UTC) : null)
				.createdAt(abTest.getCreatedAt().atOffset(java.time.ZoneOffset.UTC))
				.updatedAt(abTest.getUpdatedAt().atOffset(java.time.ZoneOffset.UTC))
				.build();
	}

	public PageResponse<AbTestOutput> toPageOutput(Page<AbTest> abTestPage) {
		if (abTestPage == null) {
			return PageResponse.empty();
		}

		return PageResponse.of(abTestPage.map(this::toOutput));
	}

	public AbTestStatisticsOutput toStatisticsOutput(AbTestStatistics statistics) {
		if (statistics == null) {
			return null;
		}

		// Placeholder logic for significantResults and successRate
		// TODO: Replace with actual logic based
		return AbTestStatisticsOutput.builder()
				.campaignId(statistics.campaignId() != null ? statistics.campaignId().toString() : null)
				.totalTests(statistics.totalTests())
				.runningTests(statistics.runningTests())
				.completedTests(statistics.completedTests())
				.scheduledTests(statistics.scheduledTests())
				.cancelledTests(statistics.totalTests() - statistics.completedTests() - statistics.runningTests() - statistics.scheduledTests())
				.significantResults(statistics.typeBreakdown().splitUrl() + statistics.typeBreakdown().multivariate() + statistics.typeBreakdown().bandit()) // Placeholder logic
				.averageSignificance(statistics.averageSignificance())
				.completionRate(statistics.completionRate() != null ? Math.round(statistics.completionRate() * 100) : null)
				.averageTestDuration(statistics.durationMetrics() != null ? statistics.durationMetrics().averageDurationDays() : null)
				.successRate(statistics.completedTests() != 0 ? Math.round(((double) statistics.typeBreakdown().splitUrl() + statistics.typeBreakdown().multivariate() + statistics.typeBreakdown().bandit()) / statistics.completedTests() * 100) : 0) // Placeholder logic
				.build();


	}
}
