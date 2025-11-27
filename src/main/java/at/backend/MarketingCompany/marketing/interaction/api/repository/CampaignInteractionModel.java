package at.backend.MarketingCompany.marketing.interaction.api.repository;

import at.backend.MarketingCompany.crm.deal.repository.persistence.model.DealEntity;
import at.backend.MarketingCompany.customer.api.repository.CustomerModel;
import at.backend.MarketingCompany.marketing.campaign.api.repository.MarketingCampaignModel;
import at.backend.MarketingCompany.common.utils.Enums.MarketingCampaign.MarketingInteractionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "campaign_interactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CampaignInteractionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", nullable = false)
    private MarketingCampaignModel campaign;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerModel customer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MarketingInteractionType interactionType;

    @Column(name = "interaction_date", nullable = false)
    private LocalDateTime interactionDate;

    @Column(name = "source_channel")
    private String sourceChannel;

    @Column(name = "source_medium")
    private String sourceMedium;

    @Column(name = "source_campaign")
    private String sourceCampaign;

    @Column(name = "device_type")
    private String deviceType;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "geo_location")
    private String geoLocation;

    @ElementCollection
    @CollectionTable(
            name = "interaction_properties",
            joinColumns = @JoinColumn(name = "interaction_id")
    )
    @MapKeyColumn(name = "property_name")
    @Column(name = "property_value")
    private Map<String, String> properties;

    @Column(length = 1000)
    private String details;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resulted_deal_id")
    private DealEntity resultedDealEntity;

    @Column(name = "conversion_value")
    private Double conversionValue;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (interactionDate == null) {
            interactionDate = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}