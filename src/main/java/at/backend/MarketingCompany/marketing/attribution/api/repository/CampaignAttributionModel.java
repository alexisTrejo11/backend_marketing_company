package at.backend.MarketingCompany.marketing.attribution.api.repository;

import at.backend.MarketingCompany.crm.deal.repository.persistence.model.DealEntity;
import at.backend.MarketingCompany.marketing.campaign.api.repository.MarketingCampaignModel;
import at.backend.MarketingCompany.common.utils.Enums.MarketingCampaign.AttributionModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "campaign_attributions")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CampaignAttributionModel {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deal_id", nullable = false)
    private DealEntity dealEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", nullable = false)
    private MarketingCampaignModel campaign;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttributionModel attributionModel;

    @Column(name = "attribution_percentage", precision = 5, scale = 2)
    private BigDecimal attributionPercentage;

    @Column(name = "attributed_revenue", precision = 19, scale = 2)
    private BigDecimal attributedRevenue;

    @Column(name = "first_touch_date")
    private LocalDateTime firstTouchDate;

    @Column(name = "last_touch_date")
    private LocalDateTime lastTouchDate;

    @Column(name = "touch_count")
    private Integer touchCount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}