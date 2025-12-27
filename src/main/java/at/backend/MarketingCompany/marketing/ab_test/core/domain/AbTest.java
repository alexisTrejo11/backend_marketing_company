package at.backend.MarketingCompany.marketing.ab_test.core.domain;

import at.backend.MarketingCompany.marketing.ab_test.core.domain.exception.AbTestBusinessRuleException;
import at.backend.MarketingCompany.marketing.ab_test.core.domain.exception.AbTestValidationException;
import at.backend.MarketingCompany.marketing.ab_test.core.domain.valueobject.AbTestId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.TestType;
import at.backend.MarketingCompany.shared.domain.BaseDomainEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class AbTest extends BaseDomainEntity<AbTestId> {
	private MarketingCampaignId campaignId;
	private String testName;
	private TestType testType;
	private Integer requiredSampleSize;
	private String controlVariant;
	private Object treatmentVariantsJson;
	private String winningVariant;
	private BigDecimal statisticalSignificance;
	private boolean isCompleted;
	private LocalDateTime startDate;
	private LocalDateTime endDate;

	@Setter
	private String hypothesis;

	@Setter
	private String primaryMetric;
	private BigDecimal confidenceLevel;


	private AbTest() {
		this.confidenceLevel = new BigDecimal("0.950");
		this.isCompleted = false;
	}

	public AbTest(AbTestId id) {
		super(id);
		this.confidenceLevel = new BigDecimal("0.950");
		this.isCompleted = false;
	}

	public static AbTest create(AbTestCreateParams params) {

		AbTestValidator.validateForCreation(
				params.testName(),
				params.startDate(),
				params.controlVariant(),
				params.treatmentVariants(),
				params.campaignId()
		);

		AbTest test = new AbTest(AbTestId.generate());
		test.campaignId = params.campaignId();
		test.testName = params.testName();
		test.testType = params.testType();
		test.primaryMetric = params.primaryMetric();
		test.controlVariant = params.controlVariant();
		test.treatmentVariantsJson = params.treatmentVariants();
		test.startDate = params.startDate();
		test.confidenceLevel = new BigDecimal("0.950");
		test.isCompleted = false;

		if (params.requiredSampleSize() != null) {
			AbTestValidator.validateSampleSize(params.requiredSampleSize());
			test.requiredSampleSize = params.requiredSampleSize();
		}
		if (params.confidenceLevel() != null) {
			AbTestValidator.validateConfidenceLevel(params.confidenceLevel());
			test.confidenceLevel = params.confidenceLevel();
		}

		return test;
	}

	public static AbTest reconstruct(AbTestReconstructParams params) {
		if (params == null) {
			return null;
		}

		AbTest test = new AbTest();
		test.id = params.id();
		test.campaignId = params.campaignId();
		test.testName = params.testName();
		test.hypothesis = params.hypothesis();
		test.testType = params.testType();
		test.primaryMetric = params.primaryMetric();
		test.confidenceLevel = params.confidenceLevel() != null ? params.confidenceLevel() : new BigDecimal("0.950");
		test.requiredSampleSize = params.requiredSampleSize();
		test.controlVariant = params.controlVariant();
		test.treatmentVariantsJson = params.treatmentVariantsJson();
		test.winningVariant = params.winningVariant();
		test.statisticalSignificance = params.statisticalSignificance();
		test.isCompleted = params.isCompleted() != null ? params.isCompleted() : false;
		test.startDate = params.startDate();
		test.endDate = params.endDate();
		test.createdAt = params.createdAt();
		test.updatedAt = params.updatedAt();
		test.deletedAt = params.deletedAt();
		test.version = params.version();

		return test;
	}

	public void complete(String winningVariant, BigDecimal statisticalSignificance) {
		if (isCompleted) {
			throw new AbTestValidationException("Test is already completed");
		}
		if (winningVariant == null || winningVariant.isBlank()) {
			throw new AbTestBusinessRuleException("Winning variant is required to complete test");
		}
		if (statisticalSignificance == null ||
				statisticalSignificance.compareTo(BigDecimal.ZERO) < 0 ||
				statisticalSignificance.compareTo(BigDecimal.ONE) > 0) {
			throw new AbTestBusinessRuleException("Statistical significance must be between 0 and 1");
		}

		this.winningVariant = winningVariant;
		this.statisticalSignificance = statisticalSignificance;
		this.isCompleted = true;
		this.endDate = LocalDateTime.now();
	}

	public boolean hasStarted() {
		return startDate != null && !LocalDateTime.now().isBefore(startDate);
	}

	public boolean hasEnded() {
		return endDate != null && !LocalDateTime.now().isBefore(endDate);
	}


	@Override
	public void softDelete() {
		if (this.hasStarted() && !this.isCompleted()) {
			throw new AbTestValidationException("Cannot delete a running AB test. Please complete it first.");
		}
		super.softDelete();
	}

	public void setEndDate(LocalDateTime endDate) {
		if (endDate == null) {
			throw new AbTestValidationException("End date cannot be null");
		}

		if (endDate.isBefore(this.startDate)) {
			throw new AbTestBusinessRuleException("End date cannot be before start date");
		}

		this.endDate = endDate;
	}

	public void setRequiredSampleSize(Integer requiredSampleSize) {
		AbTestValidator.validateSampleSize(requiredSampleSize);
		this.requiredSampleSize = requiredSampleSize;
	}

	public void setRequiredSampleSize(int requiredSampleSize) {
		AbTestValidator.validateSampleSize(requiredSampleSize);
		this.requiredSampleSize = requiredSampleSize;
	}

	public void update(
			String hypothesis,
			BigDecimal confidenceLevel,
			Integer requiredSampleSize,
			LocalDateTime endDate,
			TestType testType
	) {
		AbTestValidator.validateForUpdate(this);

		if (hypothesis != null) {
			this.hypothesis = hypothesis;
		}

		if (confidenceLevel != null) {
			AbTestValidator.validateConfidenceLevel(confidenceLevel);
			this.confidenceLevel = confidenceLevel;
		}

		if (requiredSampleSize != null) {
			AbTestValidator.validateSampleSize(requiredSampleSize);
			this.requiredSampleSize = requiredSampleSize;
		}

		if (endDate != null) {
			if (endDate.isBefore(this.startDate)) {
				throw new AbTestBusinessRuleException("End date cannot be before start date");
			}
			this.endDate = endDate;
		}

		if (testType != null) {
			this.testType = testType;
		}
	}

}