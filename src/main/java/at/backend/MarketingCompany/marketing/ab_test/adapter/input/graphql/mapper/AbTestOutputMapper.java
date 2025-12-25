package at.backend.MarketingCompany.marketing.ab_test.adapter.input.graphql.mapper;

import at.backend.MarketingCompany.marketing.ab_test.adapter.input.graphql.dto.AbTestOutput;
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
				.testType(abTest.getTestType() != null ? abTest.getTestType().name() : null)
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
				.startDate(abTest.getStartDate())
				.endDate(abTest.getEndDate())
				.createdAt(abTest.getCreatedAt())
				.updatedAt(abTest.getUpdatedAt())
				.build();
	}

	public PageResponse<AbTestOutput> toPageOutput(Page<AbTest> abTestPage) {
		if (abTestPage == null) {
			return PageResponse.empty();
		}

		return PageResponse.of(abTestPage.map(this::toOutput));
	}
}
