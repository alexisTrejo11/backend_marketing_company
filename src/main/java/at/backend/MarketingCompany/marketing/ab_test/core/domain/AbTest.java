package at.backend.MarketingCompany.marketing.ab_test.core.domain;

import at.backend.MarketingCompany.marketing.ab_test.core.domain.valueobject.AbTestId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.exception.InvalidCampaignStateException;
import at.backend.MarketingCompany.marketing.campaign.core.domain.exception.MarketingDomainException;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.TestType;
import at.backend.MarketingCompany.shared.domain.BaseDomainEntity;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class AbTest extends BaseDomainEntity<AbTestId> {
	private MarketingCampaignId campaignId;
	private String testName;
	private String hypothesis;
	private TestType testType;
	private String primaryMetric;
	private BigDecimal confidenceLevel;
	private Integer requiredSampleSize;
	private String controlVariant;
	private Map<String, Object> treatmentVariants;
	private String winningVariant;
	private BigDecimal statisticalSignificance;
	private boolean isCompleted;
	private LocalDateTime startDate;
	private LocalDateTime endDate;

	private AbTest() {
		this.confidenceLevel = new BigDecimal("0.950");
		this.isCompleted = false;
	}

	public AbTest(AbTestId id) {
		super(id);
		this.confidenceLevel = new BigDecimal("0.950");
		this.isCompleted = false;
	}

	public static AbTest create(
			MarketingCampaignId campaignId,
			String testName,
			TestType testType,
			String primaryMetric,
			String controlVariant,
			Map<String, Object> treatmentVariants,
			LocalDateTime startDate) {

		if (campaignId == null) {
			throw new MarketingDomainException("Campaign ID is required");
		}
		if (testName == null || testName.isBlank()) {
			throw new MarketingDomainException("Test name is required");
		}
		if (testType == null) {
			throw new MarketingDomainException("Test type is required");
		}
		if (primaryMetric == null || primaryMetric.isBlank()) {
			throw new MarketingDomainException("Primary metric is required");
		}
		if (controlVariant == null || controlVariant.isBlank()) {
			throw new MarketingDomainException("Control variant is required");
		}
		if (treatmentVariants == null || treatmentVariants.isEmpty()) {
			throw new MarketingDomainException("At least one treatment variant is required");
		}
		if (startDate == null) {
			throw new MarketingDomainException("Start date is required");
		}

		AbTest test = new AbTest(AbTestId.generate());
		test.campaignId = campaignId;
		test.testName = testName;
		test.testType = testType;
		test.primaryMetric = primaryMetric;
		test.controlVariant = controlVariant;
		test.treatmentVariants = treatmentVariants;
		test.startDate = startDate;
		test.confidenceLevel = new BigDecimal("0.950");
		test.isCompleted = false;

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
		test.treatmentVariants = params.treatmentVariants();
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
			throw new InvalidCampaignStateException("Test is already completed");
		}
		if (winningVariant == null || winningVariant.isBlank()) {
			throw new MarketingDomainException("Winning variant is required to complete test");
		}
		if (statisticalSignificance == null ||
				statisticalSignificance.compareTo(BigDecimal.ZERO) < 0 ||
				statisticalSignificance.compareTo(BigDecimal.ONE) > 0) {
			throw new MarketingDomainException("Statistical significance must be between 0 and 1");
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

}


