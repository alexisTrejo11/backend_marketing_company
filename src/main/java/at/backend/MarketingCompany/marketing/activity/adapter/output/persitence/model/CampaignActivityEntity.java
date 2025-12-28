package at.backend.MarketingCompany.marketing.activity.adapter.output.persitence.model;

import at.backend.MarketingCompany.account.user.adapters.outbound.persistence.UserEntity;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivityStatus;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivityType;
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
import java.util.Map;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "campaign_activities", indexes = {
    @Index(name = "idx_activities_campaign_status", columnList = "campaign_id, status, planned_start_date"),
    @Index(name = "idx_activities_assigned_user", columnList = "assigned_to_user_id, status")
})
public class CampaignActivityEntity extends BaseJpaEntity {
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "campaign_id", nullable = false)
  private MarketingCampaignEntity campaign;

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "description", length = 1000)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "activity_type", nullable = false, length = 50)
  private ActivityType activityType;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 30)
  private ActivityStatus status;

  @Column(name = "planned_start_date", nullable = false)
  private LocalDateTime plannedStartDate;

  @Column(name = "planned_end_date", nullable = false)
  private LocalDateTime plannedEndDate;

  @Column(name = "actual_start_date")
  private LocalDateTime actualStartDate;

  @Column(name = "actual_end_date")
  private LocalDateTime actualEndDate;

  @Column(name = "planned_cost", nullable = false, precision = 10, scale = 2)
  private BigDecimal plannedCost;

  @Column(name = "actual_cost", precision = 10, scale = 2)
  private BigDecimal actualCost;

  @Column(name = "cost_overrun_percentage", precision = 5, scale = 2)
  private BigDecimal costOverrunPercentage;

  @Column(name = "assigned_to_user_id")
  private Long assignedToUserId;

  @Column(name = "delivery_channel", nullable = false, length = 50)
  private String deliveryChannel;

  @Column(name = "success_criteria", length = 500)
  private String successCriteria;

  @Column(name = "target_audience", length = 500)
  private String targetAudience;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "dependencies", columnDefinition = "jsonb")
  private Object dependencies;
}
