package at.backend.MarketingCompany.marketing.ab_test.adapter.input.graphql.dto;

import at.backend.MarketingCompany.marketing.ab_test.core.application.command.CreateAbTestCommand;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.TestType;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateAbTestInput(
		@NotNull(message = "Campaign ID is required")
		@Positive(message = "Campaign ID must be positive")
		Long campaignId,

		@NotBlank(message = "Test name is required")
		@Length(min = 3, max = 200, message = "Test name must be between 3 and 200 characters")
		String testName,

		@NotBlank(message = "Control variant is required")
		@Length(min = 2, max = 200, message = "Control variant must be between 2 and 200 characters")
		String controlVariant,

		@NotNull(message = "Treatment variants are required")
		JsonNode treatmentVariants,

		@NotNull(message = "Start date is required")
		@FutureOrPresent(message = "Start date must be in the present or future")
		LocalDateTime startDate,

		@Length(max = 1000, message = "Hypothesis cannot exceed 1000 characters")
		String hypothesis,

		TestType testType,

		@Length(max = 100, message = "Primary metric cannot exceed 100 characters")
		String primaryMetric,

		@DecimalMin(value = "0.8", message = "Confidence level must be at least 0.8")
		@DecimalMax(value = "0.999", message = "Confidence level cannot exceed 0.999")
		BigDecimal confidenceLevel,

		@Min(value = 100, message = "Sample size must be at least 100")
		Integer requiredSampleSize,

		@Length(max = 200, message = "Winning variant cannot exceed 200 characters")
		String winningVariant,

		@DecimalMin(value = "0.0", message = "Statistical significance must be at least 0.0")
		@DecimalMax(value = "1.0", message = "Statistical significance cannot exceed 1.0")
		BigDecimal statisticalSignificance,

		@Future(message = "End date must be in the future")
		LocalDateTime endDate
) {

	public CreateAbTestCommand toCommand() {
		validateBusinessRules();

		return CreateAbTestCommand.builder()
				.campaignId(new MarketingCampaignId(campaignId))
				.testName(testName.trim())
				.hypothesis(hypothesis != null ? hypothesis.trim() : null)
				.testType(testType)
				.primaryMetric(primaryMetric != null ? primaryMetric.trim() : null)
				.confidenceLevel(confidenceLevel)
				.requiredSampleSize(requiredSampleSize)
				.controlVariant(controlVariant.trim())
				.winningVariant(winningVariant != null ? winningVariant.trim() : null)
				.statisticalSignificance(statisticalSignificance)
				.treatmentVariants(treatmentVariants)
				.startDate(startDate)
				.endDate(endDate)
				.build();
	}

	private void validateBusinessRules() {
		if (endDate != null && startDate != null && endDate.isBefore(startDate)) {
			throw new IllegalArgumentException("End date cannot be before start date");
		}

		if (confidenceLevel != null && requiredSampleSize != null) {
			// Rule: For confidence levels ≥ 0.95, sample size must be at least 500
			if (confidenceLevel.compareTo(new BigDecimal("0.95")) >= 0 && requiredSampleSize < 500) {
				throw new IllegalArgumentException(
						"For confidence levels ≥ 0.95, sample size must be at least 500"
				);
			}
		}
	}
}