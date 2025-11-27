package at.backend.MarketingCompany.marketing.campaign.api.repository;

import at.backend.MarketingCompany.crm.deal.repository.persistence.model.DealEntity;
import at.backend.MarketingCompany.marketing.activity.api.repository.CampaignActivityModel;
import at.backend.MarketingCompany.marketing.interaction.api.repository.CampaignInteractionModel;
import at.backend.MarketingCompany.marketing.customer.CustomerSegment;
import at.backend.MarketingCompany.common.utils.Enums.MarketingCampaign.CampaignStatus;
import at.backend.MarketingCompany.common.utils.Enums.MarketingCampaign.CampaignType;
import at.backend.MarketingCompany.marketing.metric.api.repository.CampaignMetricModel;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "marketing_campaigns")
@Data
@Builder
public class MarketingCampaignModel {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "budget", precision = 19, scale = 2)
    private BigDecimal budget;

    @Column(name = "cost_to_date", precision = 19, scale = 2)
    private BigDecimal costToDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CampaignStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CampaignType type;

    @Column(name = "target_audience", length = 500)
    private String targetAudience;

    @Column(name = "success_criteria", length = 500)
    private String successCriteria;

    @ElementCollection
    @CollectionTable(
            name = "campaign_targets",
            joinColumns = @JoinColumn(name = "campaign_id")
    )
    @MapKeyColumn(name = "metric_name")
    @Column(name = "target_value")
    private Map<String, Double> targets;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CampaignInteractionModel> interactions;

    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CampaignMetricModel> metrics;

    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CampaignActivityModel> activities;

    @ManyToMany
    @JoinTable(
            name = "campaign_deals",
            joinColumns = @JoinColumn(name = "campaign_id"),
            inverseJoinColumns = @JoinColumn(name = "deal_id")
    )
    private List<DealEntity> relatedDealEntities;

    @ManyToMany
    @JoinTable(
            name = "campaign_segments",
            joinColumns = @JoinColumn(name = "campaign_id"),
            inverseJoinColumns = @JoinColumn(name = "segment_id")
    )
    private List<CustomerSegment> targetSegments;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = CampaignStatus.DRAFT;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }


    public boolean isActive() {
        return status == CampaignStatus.ACTIVE;
    }

    public boolean isCompleted() {
        return status == CampaignStatus.COMPLETED;
    }
}