package at.backend.MarketingCompany.marketing.ab_test.adapter.input.graphql.dto;

import lombok.Builder;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A/B test statistics for a campaign.
 */
@Builder
public record AbTestStatisticsOutput(
		/**
		 * Campaign ID
		 */
		String campaignId,

		/**
		 * Total tests
		 */
		Long totalTests,

		/**
		 * Running tests
		 */
		Long runningTests,

		/**
		 * Completed tests
		 */
		Long completedTests,

		/**
		 * Scheduled tests
		 */
		Long scheduledTests,

		/**
		 * Cancelled tests
		 */
		Long cancelledTests,

		/**
		 * Tests with significant results
		 */
		Long significantResults,

		/**
		 * Average statistical significance
		 */
		BigDecimal averageSignificance,

		/**
		 * Completion rate percentage
		 */
		Long completionRate,

		/**
		 * Average test duration in days
		 */
		Long averageTestDuration,

		/**
		 * Success rate (significant results / completed)
		 */
		Long successRate
) {
}
