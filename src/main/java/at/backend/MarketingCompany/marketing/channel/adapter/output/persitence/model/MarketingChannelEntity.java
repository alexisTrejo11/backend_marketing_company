package at.backend.MarketingCompany.marketing.channel.adapter.output.persitence.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import at.backend.MarketingCompany.marketing.campaign.adapter.output.persistence.entity.MarketingCampaignEntity;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.ChannelType;
import at.backend.MarketingCompany.marketing.interaction.adapter.output.persitence.model.CampaignInteractionEntity;
import at.backend.MarketingCompany.shared.jpa.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "marketing_channels", indexes = {
    @Index(name = "idx_channel_type", columnList = "channel_type"),
    @Index(name = "idx_channel_active", columnList = "is_active")
})
public class MarketingChannelEntity extends BaseJpaEntity {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel_type", nullable = false, length = 50)
    private ChannelType channelType;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "default_cost_per_click", precision = 10, scale = 2)
    private BigDecimal defaultCostPerClick;

    @Column(name = "default_cost_per_impression", precision = 10, scale = 4)
    private BigDecimal defaultCostPerImpression;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @OneToMany(mappedBy = "primaryChannel", fetch = FetchType.LAZY)
    private Set<MarketingCampaignEntity> campaigns = new HashSet<>();

    @OneToMany(mappedBy = "channel", fetch = FetchType.LAZY)
    private Set<CampaignInteractionEntity> interactions = new HashSet<>();

    public MarketingChannelEntity(Long id) {
        this.setId(id);
    }

}
