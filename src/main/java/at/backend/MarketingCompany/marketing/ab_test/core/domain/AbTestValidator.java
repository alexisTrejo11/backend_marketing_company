package at.backend.MarketingCompany.marketing.ab_test.core.domain;

import at.backend.MarketingCompany.marketing.ab_test.core.domain.exception.AbTestValidationException;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.shared.domain.ValidationResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class AbTestValidator {
	private static final int MIN_SAMPLE_SIZE = 100;
	private static final int MAX_SAMPLE_SIZE = 1000000;
	private static final BigDecimal MIN_CONFIDENCE_LEVEL = new BigDecimal("0.8");
	private static final BigDecimal MAX_CONFIDENCE_LEVEL = new BigDecimal("0.999");
	private static final int MIN_TEST_DURATION_DAYS = 1;
	private static final int MAX_TEST_DURATION_DAYS = 180;
	private static final int TEST_NAME_MIN_LENGTH = 3;
	private static final int TEST_NAME_MAX_LENGTH = 200;
	private static final int VARIANT_NAME_MAX_LENGTH = 200;
	private static final int VARIANT_NAME_MIN_LENGTH = 2;
	private static final int MAX_TREATMENT_VARIANTS = 10;
	private static final BigDecimal[] TYPICAL_CONFIDENCE_LEVELS = {
			new BigDecimal("0.90"),
			new BigDecimal("0.95"),
			new BigDecimal("0.99")
	};

	public static ValidationResult validateForCreation(AbTestCreateParams params) {
		ValidationResult result = new ValidationResult();

		validateTestName(params.testName(), result);
		validateStartDate(params.startDate(), result);
		validateControlVariant(params.controlVariant(), result);
		validateTreatmentVariants(params.treatmentVariants(), params.controlVariant(), result);
		validateCampaignId(params.campaignId(), result);
		validateSampleSize(params.requiredSampleSize(), result);
		validateConfidenceLevel(params.confidenceLevel(), result);
		validateDates(params.startDate(), params.endDate(), result);
		validateWinningVariant(params.winningVariant(), params.treatmentVariants(),
				params.controlVariant(), result);

		return result;
	}

	private static void validateTestName(String testName, ValidationResult result) {
		if (testName == null || testName.isBlank()) {
			result.addError("Test name is required");
			return;
		}

		String trimmed = testName.trim();
		if (trimmed.length() < TEST_NAME_MIN_LENGTH) {
			result.addError("Test name must be at least 3 characters");
		}
		if (trimmed.length() > TEST_NAME_MAX_LENGTH) {
			result.addError("Test name cannot exceed 200 characters");
		}

		if (trimmed.contains("  ")) {
			result.addWarning("Test name contains multiple consecutive spaces");
		}
	}

	private static void validateStartDate(LocalDateTime startDate, ValidationResult result) {
		if (startDate == null) {
			result.addError("Start date is required");
			return;
		}

		LocalDateTime now = LocalDateTime.now();
		if (startDate.isBefore(now.minusMinutes(5))) {
			result.addError("Start date cannot be in the past");
		}
	}

	private static void validateControlVariant(String controlVariant, ValidationResult result) {
		if (controlVariant == null || controlVariant.isBlank()) {
			result.addError("Control variant is required");
			return;
		}

		String trimmed = controlVariant.trim();
		if (trimmed.length() < VARIANT_NAME_MIN_LENGTH) {
			result.addError("Control variant must be at least 2 characters");
		}
		if (trimmed.length() > VARIANT_NAME_MAX_LENGTH) {
			result.addError("Control variant cannot exceed 200 characters");
		}
	}

	private static void validateTreatmentVariants(JsonNode variants, String controlVariant,
	                                              ValidationResult result) {
		if (variants == null || variants.isNull()) {
			result.addError("Treatment variants are required");
			return;
		}

		if (!variants.isArray()) {
			result.addError("Treatment variants must be an array");
			return;
		}

		ArrayNode array = (ArrayNode) variants;
		validateTreatmentVariantsArray(array, controlVariant, result);
	}

	private static void validateTreatmentVariantsArray(ArrayNode array, String controlVariant,
	                                                   ValidationResult result) {
		if (array.isEmpty()) {
			result.addError("At least one treatment variant is required");
			return;
		}

		if (array.size() > MAX_TREATMENT_VARIANTS) {
			result.addError("Maximum 10 treatment variants allowed");
		}

		Set<String> variantNames = new HashSet<>();
		variantNames.add(controlVariant.trim().toLowerCase());

		for (int i = 0; i < array.size(); i++) {
			validateTreatmentVariant(array.get(i), i, variantNames, result);
		}
	}

	private static void validateTreatmentVariant(JsonNode variant, int index,
	                                             Set<String> variantNames, ValidationResult result) {
		if (variant == null || variant.isNull()) {
			result.addError(String.format("Treatment variant at position %d cannot be null", index));
			return;
		}

		if (!variant.isObject()) {
			result.addError(String.format("Treatment variant at position %d must be an object", index));
			return;
		}

		ObjectNode variantObj = (ObjectNode) variant;
		validateVariantName(variantObj, index, variantNames, result);
		validateVariantWeight(variantObj, index, result);
	}

	private static void validateVariantName(ObjectNode variantObj, int index,
	                                        Set<String> variantNames, ValidationResult result) {
		if (!variantObj.has("name") || variantObj.get("name").isNull()) {
			result.addError(String.format("Treatment variant at position %d must have a name", index));
			return;
		}

		String variantName = variantObj.get("name").asText().trim();
		if (variantName.isEmpty()) {
			result.addError(String.format("Treatment variant at position %d name cannot be empty", index));
			return;
		}

		if (variantName.length() > VARIANT_NAME_MAX_LENGTH) {
			result.addError(String.format("Treatment variant name cannot exceed 200 characters: %s", variantName.substring(0, 50) + "..."));
		}

		String lowerVariantName = variantName.toLowerCase();
		if (variantNames.contains(lowerVariantName)) {
			result.addError(String.format("Duplicate variant name: %s", variantName));
		}
		variantNames.add(lowerVariantName);
	}

	private static void validateVariantWeight(ObjectNode variantObj, int index, ValidationResult result) {
		if (!variantObj.has("weight")) {
			return;
		}

		JsonNode weightNode = variantObj.get("weight");
		if (weightNode.isNumber()) {
			double weight = weightNode.asDouble();
			if (weight < 0 || weight > 1) {
				String variantName = variantObj.has("name") ?
						variantObj.get("name").asText() : "variant at position " + index;
				result.addError(String.format("Variant weight must be between 0 and 1: %s", variantName));
			}
		}
	}

	private static void validateCampaignId(MarketingCampaignId campaignId, ValidationResult result) {
		if (campaignId == null) {
			result.addError("Campaign ID is required");
			return;
		}

		if (campaignId.getValue() == null || campaignId.getValue() <= 0) {
			result.addError("Campaign ID must be a positive number");
		}
	}

	public static void validateSampleSize(Integer sampleSize) {
		if (sampleSize == null) {
			return;
		}

		if (sampleSize < MIN_SAMPLE_SIZE) {
			throw new AbTestValidationException(
					String.format("Sample size must be at least %d for valid results", MIN_SAMPLE_SIZE)
			);
		}

		if (sampleSize > MAX_SAMPLE_SIZE) {
			throw new AbTestValidationException(
					String.format("Sample size cannot exceed %d", MAX_SAMPLE_SIZE)
			);
		}
	}

	private static void validateSampleSize(Integer sampleSize, ValidationResult result) {
		if (sampleSize == null) {
			return;
		}

		if (sampleSize < MIN_SAMPLE_SIZE) {
			result.addError(String.format("Sample size must be at least %d for valid results", MIN_SAMPLE_SIZE));
		}

		if (sampleSize > MAX_SAMPLE_SIZE) {
			result.addError(String.format("Sample size cannot exceed %d", MAX_SAMPLE_SIZE));
		}
	}

	public static void validateConfidenceLevel(BigDecimal confidenceLevel) {
		if (confidenceLevel == null) {
			return;
		}

		if (confidenceLevel.compareTo(MIN_CONFIDENCE_LEVEL) < 0) {
			throw new AbTestValidationException(
					String.format("Confidence level must be at least %.1f", MIN_CONFIDENCE_LEVEL)
			);
		}

		if (confidenceLevel.compareTo(MAX_CONFIDENCE_LEVEL) > 0) {
			throw new AbTestValidationException(
					String.format("Confidence level cannot exceed %.3f", MAX_CONFIDENCE_LEVEL)
			);
		}
	}

	private static void validateConfidenceLevel(BigDecimal confidenceLevel, ValidationResult result) {
		if (confidenceLevel == null) {
			return;
		}

		if (confidenceLevel.compareTo(MIN_CONFIDENCE_LEVEL) < 0) {
			result.addError(String.format("Confidence level must be at least %.1f", MIN_CONFIDENCE_LEVEL));
		}

		if (confidenceLevel.compareTo(MAX_CONFIDENCE_LEVEL) > 0) {
			result.addError(String.format("Confidence level cannot exceed %.3f", MAX_CONFIDENCE_LEVEL));
		}

		// Warning for non-typical values - simplified
		boolean isTypical = false;
		for (BigDecimal typical : TYPICAL_CONFIDENCE_LEVELS) {
			if (confidenceLevel.compareTo(typical) == 0) {
				isTypical = true;
				break;
			}
		}

		if (!isTypical) {
			result.addWarning(String.format("Confidence level %s is not typical (recommended: 0.90, 0.95, 0.99)",
					confidenceLevel));
		}
	}

	private static void validateDates(LocalDateTime startDate, LocalDateTime endDate, ValidationResult result) {
		if (startDate == null || endDate == null) {
			return;
		}

		if (endDate.isBefore(startDate)) {
			result.addError("End date cannot be before start date");
			return;
		}

		long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
		validateTestDuration(daysBetween, result);
	}

	private static void validateTestDuration(long daysBetween, ValidationResult result) {
		if (daysBetween < MIN_TEST_DURATION_DAYS) {
			result.addError(String.format("Test duration must be at least %d day(s)", MIN_TEST_DURATION_DAYS));
		}

		if (daysBetween > MAX_TEST_DURATION_DAYS) {
			result.addError(String.format("Test duration cannot exceed %d days", MAX_TEST_DURATION_DAYS));
		}
	}

	private static void validateWinningVariant(String winningVariant, JsonNode treatmentVariants,
	                                           String controlVariant, ValidationResult result) {
		if (winningVariant == null || winningVariant.isBlank()) {
			return;
		}

		if (!isValidVariant(winningVariant, treatmentVariants, controlVariant)) {
			result.addError(String.format("Winning variant '%s' must be either control variant or one of the treatment variants",
					winningVariant));
		}
	}

	private static boolean isValidVariant(String variant, JsonNode treatmentVariants, String controlVariant) {
		Set<String> validVariants = new HashSet<>();
		validVariants.add(controlVariant.trim().toLowerCase());

		if (treatmentVariants != null && treatmentVariants.isArray()) {
			for (JsonNode treatmentVariant : treatmentVariants) {
				if (treatmentVariant.has("name")) {
					validVariants.add(treatmentVariant.get("name").asText().trim().toLowerCase());
				}
			}
		}

		return validVariants.contains(variant.trim().toLowerCase());
	}

	public static void validateForUpdate(AbTest existingTest, AbTestUpdateParams updateParams) {
		if (existingTest == null) {
			throw new AbTestValidationException("Test cannot be null");
		}

		if (existingTest.isCompleted()) {
			throw new AbTestValidationException("Cannot update a completed test");
		}

		ValidationResult result = new ValidationResult();
		validateRunningTestUpdate(existingTest, updateParams, result);

		if (updateParams.confidenceLevel() != null) {
			validateConfidenceLevel(updateParams.confidenceLevel(), result);
		}

		if (updateParams.requiredSampleSize() != null) {
			validateSampleSize(updateParams.requiredSampleSize(), result);
		}

		if (updateParams.endDate() != null) {
			validateEndDateForUpdate(existingTest, updateParams.endDate(), result);
		}

		if (!result.isValid()) {
			throw new AbTestValidationException(result.getErrorsAsString());
		}
	}

	private static void validateRunningTestUpdate(AbTest test, AbTestUpdateParams updateParams,
	                                              ValidationResult result) {
		if (!test.hasStarted()) {
			return;
		}

		if (updateParams.testType() != null && updateParams.testType() != test.getTestType()) {
			result.addError("Cannot change test type while test is running");
		}

		if (updateParams.startDate() != null && !updateParams.startDate().equals(test.getStartDate())) {
			result.addError("Cannot change start date while test is running");
		}
	}

	private static void validateEndDateForUpdate(AbTest test, LocalDateTime newEndDate,
	                                             ValidationResult result) {
		if (newEndDate.isBefore(test.getStartDate())) {
			result.addError("End date cannot be before start date");
			return;
		}

		long newDuration = ChronoUnit.DAYS.between(test.getStartDate(), newEndDate);
		if (newDuration > MAX_TEST_DURATION_DAYS) {
			result.addError(String.format("Total test duration cannot exceed %d days", MAX_TEST_DURATION_DAYS));
		}
	}
}