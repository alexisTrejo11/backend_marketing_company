package at.backend.MarketingCompany.marketing.metric.adapter.output.persitence.model;

import at.backend.MarketingCompany.marketing.campaign.adapter.output.persistence.entity.MarketingCampaignEntity;
import at.backend.MarketingCompany.shared.jpa.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "campaign_metrics", indexes = {
		@Index(name = "idx_metrics_campaign_target", columnList = "campaign_id, is_target_achieved, metric_type")
})
public class CampaignMetricEntity extends BaseJpaEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "campaign_id", nullable = false)
	private MarketingCampaignEntity campaign;

	@Column(name = "name", nullable = false, length = 100)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "metric_type", nullable = false, length = 30)
	private MetricType metricType;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	@Column(name = "current_value", precision = 15, scale = 4)
	private BigDecimal currentValue = BigDecimal.ZERO;

	@Column(name = "target_value", precision = 15, scale = 4)
	private BigDecimal targetValue;

	@Column(name = "measurement_unit", length = 50)
	private String measurementUnit;

	@Column(name = "calculation_formula", length = 500)
	private String calculationFormula;

	@Column(name = "data_source", length = 200)
	private String dataSource;

	@Column(name = "last_calculated_date")
	private LocalDateTime lastCalculatedDate;

	@Column(name = "is_automated")
	private Boolean isAutomated = false;

	@Column(name = "is_target_achieved")
	private Boolean isTargetAchieved = false;

	public enum MetricType {
		COUNT, CURRENCY, PERCENTAGE, DURATION, COST, RATIO, SCORE
	}
}