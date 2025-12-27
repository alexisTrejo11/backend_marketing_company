package at.backend.MarketingCompany.marketing.ab_test.adapter.input.graphql.dto;

import at.backend.MarketingCompany.marketing.ab_test.core.application.command.CreateAbTestCommand;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.TestType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public record CreateAbTestInput(
		@NotNull Long campaignId,
		@NotNull @Length(min = 3, max = 200) String testName,
		String hypothesis,
		TestType testType,
		String primaryMetric,
		BigDecimal confidenceLevel,
		Integer requiredSampleSize,
		@NotNull @Length(min = 2, max = 200) String controlVariant,
		@NotNull @Size(min = 1, max = 50) Map<String, Object> treatmentVariants,
		@NotNull @FutureOrPresent LocalDateTime startDate
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
