package at.backend.MarketingCompany.marketing.campaign.adapter.output.persistence.entity;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.TestType;
import at.backend.MarketingCompany.shared.jpa.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ab_tests", indexes = {
    @Index(name = "idx_ab_tests_campaign_status", columnList = "campaign_id, is_completed, start_date")
})
public class AbTestEntity extends BaseJpaEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "campaign_id", nullable = false)
	private MarketingCampaignEntity campaign;

	@Column(name = "test_name", nullable = false, length = 200)
	private String testName;

	@Column(name = "hypothesis", length = 500)
	private String hypothesis;

	@Enumerated(EnumType.STRING)
	@Column(name = "test_type", nullable = false, length = 50)
	private TestType testType;

	@Column(name = "primary_metric", nullable = false, length = 100)
	private String primaryMetric;

	@Column(name = "confidence_level", precision = 4, scale = 3)
	private BigDecimal confidenceLevel = new BigDecimal("0.950");

	@Column(name = "required_sample_size")
	private Integer requiredSampleSize;

	@Column(name = "control_variant", nullable = false, length = 100)
	private String controlVariant;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "treatment_variants", nullable = false, columnDefinition = "jsonb")
	private Object treatmentVariants;

	@Column(name = "winning_variant", length = 100)
	private String winningVariant;

	@Column(name = "statistical_significance", precision = 4, scale = 3)
	private BigDecimal statisticalSignificance;

	@Column(name = "is_completed")
	private Boolean isCompleted = false;

	@Column(name = "start_date", nullable = false)
	private LocalDateTime startDate;

	@Column(name = "end_date")
	private LocalDateTime endDate;
}