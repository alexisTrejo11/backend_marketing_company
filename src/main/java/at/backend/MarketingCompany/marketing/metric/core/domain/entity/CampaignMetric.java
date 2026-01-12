package at.backend.MarketingCompany.marketing.metric.core.domain.entity;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MetricType;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.metric.core.domain.valueobject.CampaignMetricId;
import at.backend.MarketingCompany.shared.domain.BaseDomainEntity;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Getter
@Slf4j
public class CampaignMetric extends BaseDomainEntity<CampaignMetricId> {
	private MarketingCampaignId campaignId;
	private String name;
	private MetricType metricType;
	private String description;
	private BigDecimal currentValue;
	private BigDecimal targetValue;
	private String measurementUnit;
	private String calculationFormula;
	private String dataSource;
	private LocalDateTime lastCalculatedDate;
	private boolean isAutomated;
	private boolean isTargetAchieved;

	private CampaignMetric() {
		this.currentValue = BigDecimal.ZERO;
		this.isAutomated = false;
		this.isTargetAchieved = false;
	}

	public CampaignMetric(CampaignMetricId id) {
		super(id);
		this.currentValue = BigDecimal.ZERO;
		this.isAutomated = false;
		this.isTargetAchieved = false;
	}

	public static CampaignMetric create(CreateMetricParams params) {
		MetricValidator.validateForCreation(params);

		CampaignMetric metric = new CampaignMetric(CampaignMetricId.generate());
		metric.campaignId = params.campaignId();
		metric.name = params.name();
		metric.metricType = params.metricType();
		metric.measurementUnit = params.measurementUnit();
		metric.currentValue = BigDecimal.ZERO;
		metric.isAutomated = params.isAutomated();
		metric.isTargetAchieved = false;
		metric.description = params.description();
		metric.calculationFormula = params.calculationFormula();
		metric.dataSource = params.dataSource();
		metric.targetValue = params.targetValue();

		log.debug("Created CampaignMetric: campaignId={}, name={}, type={}, automated={}, target={} ",
				metric.campaignId != null ? metric.campaignId.getValue() : null,
				metric.name,
				metric.metricType,
				metric.isAutomated,
				metric.targetValue);

		return metric;
	}

	public void markAsAutomated() {
		if (!this.isAutomated) {
			this.isAutomated = true;
			log.info("Metric marked as automated: id={}, name={}", this.id != null ? this.id.getValue() : null, this.name);
		}
	}

	public static CampaignMetric reconstruct(CampaignMetricReconstructParams params) {
		if (params == null) {
			return null;
		}

		CampaignMetric metric = new CampaignMetric();
		metric.id = params.id();
		metric.campaignId = params.campaignId();
		metric.name = params.name();
		metric.metricType = params.metricType();
		metric.description = params.description();
		metric.currentValue = params.currentValue() != null ? params.currentValue() : BigDecimal.ZERO;
		metric.targetValue = params.targetValue();
		metric.measurementUnit = params.measurementUnit();
		metric.calculationFormula = params.calculationFormula();
		metric.dataSource = params.dataSource();
		metric.lastCalculatedDate = params.lastCalculatedDate();
		metric.isAutomated = params.isAutomated() != null ? params.isAutomated() : false;
		metric.isTargetAchieved = params.isTargetAchieved() != null ? params.isTargetAchieved() : false;
		metric.createdAt = params.createdAt();
		metric.updatedAt = params.updatedAt();
		metric.deletedAt = params.deletedAt();
		metric.version = params.version();

		return metric;
	}

	public void updateGeneralInfo(
			String description,
			BigDecimal targetValue,
			String calculationFormula,
			String dataSource
	) {
		MetricValidator.validateForUpdate(description, targetValue, calculationFormula, dataSource);

		this.description = description;
		this.targetValue = targetValue;
		this.calculationFormula = calculationFormula;
		this.dataSource = dataSource;

		log.info("Metric general info updated: id={}, name={}, target={}, calculationFormulaPresent={}",
				this.id != null ? this.id.getValue() : null,
				this.name,
				this.targetValue,
				this.calculationFormula != null);
	}

	public void updateValue(BigDecimal newValue) {
		MetricValidator.validateValueUpdate(newValue);

		BigDecimal previous = this.currentValue != null ? this.currentValue : BigDecimal.ZERO;
		this.currentValue = newValue;
		this.lastCalculatedDate = LocalDateTime.now();

		boolean wasAchieved = this.isTargetAchieved;
		if (this.targetValue != null) {
			this.isTargetAchieved = this.currentValue.compareTo(this.targetValue) >= 0;
		} else {
			this.isTargetAchieved = false; // cannot be achieved without a target
		}

		log.info("Metric value updated: id={}, name={}, previous={}, new={}, target={}, achieved={} -> {}",
				this.id != null ? this.id.getValue() : null,
				this.name,
				previous,
				this.currentValue,
				this.targetValue,
				wasAchieved,
				this.isTargetAchieved);
	}

	public BigDecimal achievementPercentage() {
		if (this.targetValue == null || this.targetValue.compareTo(BigDecimal.ZERO) == 0) {
			return BigDecimal.ZERO;
		}

		BigDecimal current = this.currentValue != null ? this.currentValue : BigDecimal.ZERO;
		try {
			return current.divide(this.targetValue, 4, RoundingMode.HALF_UP)
					.multiply(BigDecimal.valueOf(100));
		} catch (ArithmeticException ex) {
			log.warn("Arithmetic error while calculating achievement percentage for metric id={}: {}", this.id != null ? this.id.getValue() : null, ex.getMessage());
			return BigDecimal.ZERO;
		}
	}
}

