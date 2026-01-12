package at.backend.MarketingCompany.marketing.ab_test.core.application.command;

import at.backend.MarketingCompany.marketing.ab_test.core.domain.AbTestCreateParams;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.TestType;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Builder
public record CreateAbTestCommand(
		MarketingCampaignId campaignId,
		String testName,
		String hypothesis,
		TestType testType,
		String primaryMetric,
		BigDecimal confidenceLevel,
		Integer requiredSampleSize,
		String controlVariant,
		String winningVariant,
		BigDecimal statisticalSignificance,
		JsonNode treatmentVariants,
		LocalDateTime startDate,
		LocalDateTime endDate
) {

	public AbTestCreateParams toAbTestCreateParams() {
		return AbTestCreateParams.builder()
				.campaignId(campaignId)
				.testName(testName)
				.testType(testType)
				.primaryMetric(primaryMetric)
				.controlVariant(controlVariant)
				.treatmentVariants(treatmentVariants)
				.startDate(startDate)
				.endDate(endDate)
				.requiredSampleSize(requiredSampleSize)
				.confidenceLevel(confidenceLevel)
				.hypothesis(hypothesis)
				.winningVariant(winningVariant)
				.statisticalSignificance(statisticalSignificance)
				.build();
	}
}