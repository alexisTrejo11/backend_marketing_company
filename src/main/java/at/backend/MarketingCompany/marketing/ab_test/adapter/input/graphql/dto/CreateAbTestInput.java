package at.backend.MarketingCompany.marketing.ab_test.adapter.input.graphql.dto;

import at.backend.MarketingCompany.marketing.ab_test.core.application.command.CreateAbTestCommand;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.TestType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public record CreateAbTestInput(
		Long campaignId,
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
	public CreateAbTestCommand toCommand() {
		return new CreateAbTestCommand(
				this.campaignId != null ? new MarketingCampaignId(this.campaignId) : null,
				this.testName,
				this.hypothesis,
				this.testType,
				this.primaryMetric,
				this.confidenceLevel,
				this.requiredSampleSize,
				this.controlVariant,
				this.treatmentVariants,
				this.startDate
		);
	}
}
