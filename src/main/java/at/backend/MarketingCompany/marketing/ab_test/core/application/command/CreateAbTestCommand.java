package at.backend.MarketingCompany.marketing.ab_test.core.application.command;

import at.backend.MarketingCompany.marketing.ab_test.core.domain.AbTestCreateParams;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.TestType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public record CreateAbTestCommand(
		MarketingCampaignId campaignId,
    String testName,
    String hypothesis,
    TestType testType,
    String primaryMetric,
    BigDecimal confidenceLevel,
    Integer requiredSampleSize,
    String controlVariant,
    Map<String, Object> treatmentVariants,
    LocalDateTime startDate
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
				.requiredSampleSize(requiredSampleSize)
				.confidenceLevel(confidenceLevel)
				.build();
	}
}