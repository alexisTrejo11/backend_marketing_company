package at.backend.MarketingCompany.marketing.attribution.adapter.output.persitence.model;

import at.backend.MarketingCompany.marketing.campaign.adapter.output.persistence.entity.MarketingCampaignEntity;
import at.backend.MarketingCompany.shared.jpa.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "campaign_attribution", indexes = {
    @Index(name = "idx_attribution_deal", columnList = "deal_id, attribution_percentage"),
    @Index(name = "idx_attribution_campaign_revenue", columnList = "campaign_id, attributed_revenue")
})
public class CampaignAttributionEntity extends BaseJpaEntity {

  @Column(name = "deal_id", nullable = false)
  private Long dealId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "campaign_id", nullable = false)
  private MarketingCampaignEntity campaign;

  @Enumerated(EnumType.STRING)
  @Column(name = "attribution_model", nullable = false, length = 50)
  private AttributionModel attributionModel;

  @Column(name = "attribution_percentage", nullable = false, precision = 5, scale = 2)
  private BigDecimal attributionPercentage;

  @Column(name = "attributed_revenue", nullable = false, precision = 15, scale = 2)
  private BigDecimal attributedRevenue = BigDecimal.ZERO;

  @JdbcTypeCode(SqlTypes.ARRAY)
  @Column(name = "touch_timestamps", columnDefinition = "timestamp[]")
  private List<LocalDateTime> touchTimestamps = new ArrayList<>();

  @Column(name = "touch_count")
  private Integer touchCount = 0;

  @Column(name = "first_touch_weight", precision = 5, scale = 2)
  private BigDecimal firstTouchWeight = BigDecimal.ZERO;

  @Column(name = "last_touch_weight", precision = 5, scale = 2)
  private BigDecimal lastTouchWeight = BigDecimal.ZERO;

  @Column(name = "linear_weight", precision = 5, scale = 2)
  private BigDecimal linearWeight = BigDecimal.ZERO;

}
