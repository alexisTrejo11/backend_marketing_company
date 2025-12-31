package at.backend.MarketingCompany.marketing.ab_test.core.domain;

import at.backend.MarketingCompany.marketing.ab_test.core.domain.exception.AbTestBusinessRuleException;
import at.backend.MarketingCompany.marketing.ab_test.core.domain.exception.AbTestValidationException;
import at.backend.MarketingCompany.marketing.ab_test.core.domain.valueobject.AbTestId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.TestType;
import at.backend.MarketingCompany.shared.domain.BaseDomainEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

@Getter
@Slf4j
public class AbTest extends BaseDomainEntity<AbTestId> {
	private MarketingCampaignId campaignId;
	private String testName;
	private TestType testType;
	private Integer requiredSampleSize;
	private String controlVariant;
	private String winningVariant;
	private BigDecimal statisticalSignificance;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private BigDecimal confidenceLevel;
	private String hypothesis;
	private String primaryMetric;
	private boolean isCompleted;
	private JsonNode treatmentVariants;

	private static final BigDecimal DEFAULT_CONFIDENCE_LEVEL = new BigDecimal("0.950");
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	private static final int MAX_TEST_DURATION_DAYS = 180;
	private static final int MIN_TEST_DURATION_DAYS = 1;
	private static final int MIN_EARLY_STOP_DAYS = 7;
	private static final BigDecimal HIGH_SIGNIFICANCE_THRESHOLD = new BigDecimal("0.01");
	private static final BigDecimal WINNER_SIGNIFICANCE_THRESHOLD = new BigDecimal("0.05");
	private static final int HYPOTHESIS_MAX_LENGTH = 1000;

	private AbTest() {
		this.confidenceLevel = DEFAULT_CONFIDENCE_LEVEL;
		this.isCompleted = false;
	}

	public AbTest(AbTestId id) {
		super(id);
		this.confidenceLevel = DEFAULT_CONFIDENCE_LEVEL;
		this.isCompleted = false;
	}

	public static AbTest create(AbTestCreateParams params) {
		AbTestValidator.ValidationResult validationResult = AbTestValidator.validateForCreation(params);
		validationResult.throwIfInvalid();

		if (validationResult.hasWarnings()) {
			log.warn("Warnings during AB Test creation: {}", validationResult.getWarningsAsString());
		}

		AbTest test = new AbTest(AbTestId.generate());
		initializeTestFromParams(test, params);
		test.validateBusinessRules();

		return test;
	}

	public static AbTest reconstruct(AbTestReconstructParams params) {
		if (params == null) {
			return null;
		}

		AbTest test = new AbTest();
		populateReconstructedFields(test, params);
		return test;
	}

	public String getTreatmentVariantsJson() {
		if (treatmentVariants == null) {
			return OBJECT_MAPPER.createArrayNode().toString();
		}
		return treatmentVariants.toString();
	}

	public boolean hasStarted() {
		return startDate != null && LocalDateTime.now().isAfter(startDate);
	}

	public boolean hasEnded() {
		return endDate != null && LocalDateTime.now().isAfter(endDate);
	}

	@Override
	public void softDelete() {
		if (!canBeDeleted()) {
			throw new AbTestValidationException("Cannot delete a running AB test. Please complete it first.");
		}
		super.softDelete();
	}

	public void setEndDate(LocalDateTime endDate) {
		validateEndDate(endDate);
		this.endDate = endDate;
	}

	public void setRequiredSampleSize(Integer requiredSampleSize) {
		this.requiredSampleSize = requiredSampleSize;
	}

	public void setRequiredSampleSize(int requiredSampleSize) {
		this.requiredSampleSize = requiredSampleSize;
	}

	public void update(AbTestUpdateParams updateParams) {
		validateUpdatePreconditions();
		AbTestValidator.validateForUpdate(this, updateParams);

		boolean hasChanges = applyUpdates(updateParams);

		if (hasChanges) {
			validateBusinessRules();
			this.updatedAt = LocalDateTime.now();
		} else {
			log.debug("No changes detected in update parameters for test {}", getId());
		}
	}

	public void complete(String winningVariant, BigDecimal statisticalSignificance) {
		validateCompletionPreconditions();
		validateWinningVariant(winningVariant);
		validateStatisticalSignificance(statisticalSignificance);

		this.winningVariant = winningVariant.trim();
		this.statisticalSignificance = statisticalSignificance.setScale(4, RoundingMode.HALF_UP);
		this.isCompleted = true;
		this.endDate = LocalDateTime.now();

		validateCompletionDate();
	}

	public boolean canBeModified() {
		return !isCompleted && !hasStarted();
	}

	public boolean canBeDeleted() {
		return !hasStarted() || isCompleted;
	}

	public boolean isRunning() {
		return hasStarted() && !isCompleted && !hasEnded();
	}

	public void validateForEarlyStop(BigDecimal currentSignificance) {
		validateEarlyStopPreconditions(currentSignificance);
		validateEarlyStopRules(currentSignificance);
	}

	private static void initializeTestFromParams(AbTest test, AbTestCreateParams params) {
		test.campaignId = params.campaignId();
		test.testName = params.testName().trim();
		test.testType = params.testType();
		test.primaryMetric = params.primaryMetric() != null ? params.primaryMetric().trim() : null;
		test.controlVariant = params.controlVariant().trim();
		test.treatmentVariants = normalizeTreatmentVariants(params.treatmentVariants());
		test.startDate = params.startDate();
		test.confidenceLevel = params.confidenceLevel() != null ? params.confidenceLevel() : DEFAULT_CONFIDENCE_LEVEL;
		test.hypothesis = params.hypothesis() != null ? params.hypothesis().trim() : null;
		test.statisticalSignificance = params.statisticalSignificance();
		test.winningVariant = params.winningVariant();
		test.endDate = params.endDate();
		test.requiredSampleSize = params.requiredSampleSize();
	}

	private static void populateReconstructedFields(AbTest test, AbTestReconstructParams params) {
		test.id = params.id();
		test.campaignId = params.campaignId();
		test.testName = params.testName();
		test.hypothesis = params.hypothesis();
		test.testType = params.testType();
		test.primaryMetric = params.primaryMetric();
		test.confidenceLevel = params.confidenceLevel() != null ? params.confidenceLevel() : DEFAULT_CONFIDENCE_LEVEL;
		test.requiredSampleSize = params.requiredSampleSize();
		test.controlVariant = params.controlVariant();
		test.treatmentVariants = params.treatmentVariantsJson();
		test.winningVariant = params.winningVariant();
		test.statisticalSignificance = params.statisticalSignificance();
		test.isCompleted = params.isCompleted() != null ? params.isCompleted() : false;
		test.startDate = params.startDate();
		test.endDate = params.endDate();
		test.createdAt = params.createdAt();
		test.updatedAt = params.updatedAt();
		test.deletedAt = params.deletedAt();
		test.version = params.version();
	}

	private static JsonNode normalizeTreatmentVariants(JsonNode variants) {
		if (variants == null || !variants.isArray()) {
			return variants;
		}

		ArrayNode normalized = OBJECT_MAPPER.createArrayNode();
		for (JsonNode variant : variants) {
			if (variant.isObject()) {
				ObjectNode normalizedVariant = OBJECT_MAPPER.createObjectNode();
				variant.fields().forEachRemaining(entry -> {
					if ("name".equals(entry.getKey()) && entry.getValue().isTextual()) {
						normalizedVariant.put(entry.getKey(), entry.getValue().asText().trim());
					} else {
						normalizedVariant.set(entry.getKey(), entry.getValue());
					}
				});
				normalized.add(normalizedVariant);
			}
		}
		return normalized;
	}

	private void validateEndDate(LocalDateTime endDate) {
		if (endDate == null) {
			throw new AbTestValidationException("End date cannot be null");
		}

		if (endDate.isBefore(this.startDate)) {
			throw new AbTestBusinessRuleException("End date cannot be before start date");
		}
	}

	private void validateUpdatePreconditions() {
		if (this.isCompleted()) {
			throw new AbTestValidationException("Cannot update a completed test");
		}

		if (this.hasStarted() && !isRunning()) {
			throw new AbTestValidationException("Cannot update a test that has ended");
		}
	}

	private boolean applyUpdates(AbTestUpdateParams updateParams) {
		boolean hasChanges = false;
		hasChanges |= updateHypothesis(updateParams);
		hasChanges |= updateConfidenceLevel(updateParams);
		hasChanges |= updateRequiredSampleSize(updateParams);
		hasChanges |= updateTestType(updateParams);
		hasChanges |= updateEndDate(updateParams);
		hasChanges |= updateStartDate(updateParams);
		return hasChanges;
	}

	private boolean updateHypothesis(AbTestUpdateParams updateParams) {
		if (updateParams.hypothesis() == null) {
			return false;
		}

		if (updateParams.hypothesis().trim().isEmpty()) {
			this.hypothesis = null;
			return true;
		}

		String newHypothesis = updateParams.hypothesis().trim();
		if (newHypothesis.equals(this.hypothesis)) {
			return false;
		}

		validateHypothesis(newHypothesis);
		this.hypothesis = newHypothesis;
		return true;
	}

	private boolean updateConfidenceLevel(AbTestUpdateParams updateParams) {
		if (updateParams.confidenceLevel() == null || updateParams.confidenceLevel().equals(this.confidenceLevel)) {
			return false;
		}

		AbTestValidator.validateConfidenceLevel(updateParams.confidenceLevel());
		this.confidenceLevel = updateParams.confidenceLevel();

		if (this.requiredSampleSize != null) {
			validateSampleSizeForConfidenceLevel();
		}
		return true;
	}

	private boolean updateRequiredSampleSize(AbTestUpdateParams updateParams) {
		if (updateParams.requiredSampleSize() == null || updateParams.requiredSampleSize().equals(this.requiredSampleSize)) {
			return false;
		}

		AbTestValidator.validateSampleSize(updateParams.requiredSampleSize());
		this.requiredSampleSize = updateParams.requiredSampleSize();
		validateSampleSizeForConfidenceLevel();

		if (this.isRunning()) {
			log.warn("Changing sample size while test is running may affect results for test {}", getId());
		}
		return true;
	}

	private boolean updateTestType(AbTestUpdateParams updateParams) {
		if (updateParams.testType() == null || updateParams.testType() == this.testType) {
			return false;
		}

		if (this.hasStarted()) {
			throw new AbTestValidationException("Cannot change test type while test is running");
		}

		this.testType = updateParams.testType();
		return true;
	}

	private boolean updateEndDate(AbTestUpdateParams updateParams) {
		if (updateParams.endDate() == null || (updateParams.endDate().equals(this.endDate))) {
			return false;
		}

		setEndDate(updateParams.endDate());

		if (this.isRunning()) {
			validateEndDateReduction();
		}
		return true;
	}

	private boolean updateStartDate(AbTestUpdateParams updateParams) {
		if (updateParams.startDate() == null || updateParams.startDate().equals(this.startDate)) {
			return false;
		}

		if (this.hasStarted()) {
			throw new AbTestValidationException("Cannot change start date after test has started");
		}

		if (updateParams.startDate().isBefore(LocalDateTime.now())) {
			throw new AbTestValidationException("Start date cannot be in the past");
		}

		this.startDate = updateParams.startDate();

		if (this.endDate != null && this.endDate.isBefore(this.startDate)) {
			throw new AbTestValidationException("End date cannot be before start date");
		}
		return true;
	}

	private void validateHypothesis(String hypothesis) {
		if (hypothesis.length() > HYPOTHESIS_MAX_LENGTH) {
			throw new AbTestValidationException("Hypothesis cannot exceed 1000 characters");
		}

		if (hypothesis.length() < 10 && hypothesis.split("\\s+").length < 3) {
			log.warn("Hypothesis seems very short. Consider providing more detail for test {}", getId());
		}
	}

	private void validateEndDateReduction() {
		if (this.endDate == null || this.startDate == null) {
			return;
		}

		long originalDuration = ChronoUnit.DAYS.between(this.startDate,
				this.updatedAt != null ? this.updatedAt : LocalDateTime.now());
		long newRemainingDuration = ChronoUnit.DAYS.between(LocalDateTime.now(), this.endDate);

		if (originalDuration > 0 && newRemainingDuration < (originalDuration * 0.2)) {
			log.warn("End date has been significantly shortened for test {}. This may affect statistical validity.",
					getId());
		}

		long totalDuration = ChronoUnit.DAYS.between(this.startDate, this.endDate);
		if (totalDuration < MIN_TEST_DURATION_DAYS) {
			throw new AbTestValidationException("Test must run for at least 1 day");
		}
	}

	private void validateBusinessRules() {
		if (requiredSampleSize != null && confidenceLevel != null) {
			validateSampleSizeForConfidenceLevel();
		}

		if (endDate != null) {
			validateTestDuration();
		}

		validateVariantDistribution();
	}

	private void validateSampleSizeForConfidenceLevel() {
		int minSampleSize = calculateMinimumSampleSize().intValue();
		if (requiredSampleSize < minSampleSize) {
			throw new AbTestBusinessRuleException(String.format(
					"For confidence level %s, minimum sample size is %d",
					confidenceLevel.setScale(3, RoundingMode.HALF_UP),
					minSampleSize
			));
		}
	}

	private BigDecimal calculateMinimumSampleSize() {
		// Minimum sample size calculation based on confidence level
		// Need to use properly statistical formulas
		if (confidenceLevel.compareTo(new BigDecimal("0.90")) == 0) {
			return new BigDecimal("385");
		} else if (confidenceLevel.compareTo(new BigDecimal("0.95")) == 0) {
			return new BigDecimal("384");
		} else if (confidenceLevel.compareTo(new BigDecimal("0.99")) == 0) {
			return new BigDecimal("666");
		}
		return new BigDecimal("500");
	}

	private void validateTestDuration() {
		long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

		if (daysBetween < MIN_TEST_DURATION_DAYS) {
			throw new AbTestBusinessRuleException("Test must run for at least 1 day");
		}

		if (daysBetween > MAX_TEST_DURATION_DAYS) {
			throw new AbTestBusinessRuleException("Test cannot run for more than 180 days");
		}

		if (requiredSampleSize != null && requiredSampleSize > 10000) {
			long minDaysForLargeSample = (long) Math.ceil(requiredSampleSize / 1000.0);
			if (daysBetween < minDaysForLargeSample) {
				throw new AbTestBusinessRuleException(String.format(
						"For sample size of %d, test should run for at least %d days",
						requiredSampleSize, minDaysForLargeSample
				));
			}
		}
	}

	private void validateCompletionPreconditions() {
		if (isCompleted) {
			throw new AbTestValidationException("Test is already completed");
		}

		if (!hasStarted()) {
			throw new AbTestBusinessRuleException("Cannot complete a test that hasn't started");
		}
	}

	private void validateStatisticalSignificance(BigDecimal statisticalSignificance) {
		if (statisticalSignificance == null) {
			throw new AbTestBusinessRuleException("Statistical significance is required");
		}

		if (statisticalSignificance.compareTo(BigDecimal.ZERO) < 0 ||
				statisticalSignificance.compareTo(BigDecimal.ONE) > 0) {
			throw new AbTestBusinessRuleException("Statistical significance must be between 0 and 1");
		}

		if (statisticalSignificance.compareTo(WINNER_SIGNIFICANCE_THRESHOLD) > 0) {
			throw new AbTestBusinessRuleException(
					"Statistical significance must be ≤ 0.05 to declare a winner"
			);
		}
	}

	private void validateWinningVariant(String winningVariant) {
		if (winningVariant == null || winningVariant.isBlank()) {
			throw new AbTestBusinessRuleException("Winning variant is required");
		}

		Set<String> validVariants = getValidVariants();
		String normalizedVariant = winningVariant.trim().toLowerCase();
		if (!validVariants.contains(normalizedVariant)) {
			throw new AbTestBusinessRuleException(String.format(
					"'%s' is not a valid variant. Must be one of: %s",
					winningVariant, String.join(", ", validVariants)
			));
		}
	}

	private Set<String> getValidVariants() {
		Set<String> variants = new HashSet<>();
		variants.add(controlVariant.trim().toLowerCase());

		if (treatmentVariants != null && treatmentVariants.isArray()) {
			for (JsonNode variant : treatmentVariants) {
				if (variant.has("name")) {
					variants.add(variant.get("name").asText().trim().toLowerCase());
				}
			}
		}

		return variants;
	}

	private void validateCompletionDate() {
		if (this.endDate.isBefore(this.startDate)) {
			throw new AbTestBusinessRuleException("Completion date cannot be before start date");
		}
	}

	private void validateEarlyStopPreconditions(BigDecimal currentSignificance) {
		if (!isRunning()) {
			throw new AbTestBusinessRuleException("Only running tests can be stopped early");
		}

		if (currentSignificance == null) {
			throw new AbTestBusinessRuleException("Current significance is required");
		}
	}

	private void validateEarlyStopRules(BigDecimal currentSignificance) {
		long daysRunning = ChronoUnit.DAYS.between(startDate, LocalDateTime.now());
		if (daysRunning < MIN_EARLY_STOP_DAYS) {
			throw new AbTestBusinessRuleException(
					"Cannot stop test early before 7 days of running"
			);
		}

		if (currentSignificance.compareTo(HIGH_SIGNIFICANCE_THRESHOLD) > 0) {
			throw new AbTestBusinessRuleException(
					"Cannot stop test early without strong significance (p ≤ 0.01)"
			);
		}
	}

	private void validateVariantDistribution() {
		if (treatmentVariants == null || !treatmentVariants.isArray()) {
			return;
		}

		int variantCount = 1 + treatmentVariants.size();
		if (requiredSampleSize != null && requiredSampleSize % variantCount != 0) {
			log.warn("Sample size {} is not a multiple of variant count {} for test {}. " +
							"This may cause uneven distribution.",
					requiredSampleSize, variantCount, getId());
		}
	}
}