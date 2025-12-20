package at.backend.MarketingCompany.marketing.assets.adapter.output.persitence.model;

import at.backend.MarketingCompany.marketing.assets.core.domain.entity.AssetStatus;
import at.backend.MarketingCompany.marketing.assets.core.domain.entity.AssetType;
import at.backend.MarketingCompany.marketing.campaign.adapter.output.persistence.entity.MarketingCampaignEntity;
import at.backend.MarketingCompany.shared.jpa.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "marketing_assets", indexes = {
    @Index(name = "idx_assets_campaign_type", columnList = "campaign_id, asset_type, status")
})
public class MarketingAssetEntity extends BaseJpaEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "campaign_id", nullable = false)
  private MarketingCampaignEntity campaign;

  @Enumerated(EnumType.STRING)
  @Column(name = "asset_type", nullable = false, length = 50)
  private AssetType assetType;

  @Column(name = "name", nullable = false, length = 200)
  private String name;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @Column(name = "url", nullable = false, length = 500)
  private String url;

  @Column(name = "version", length = 20)
  private String version = "1.0";

  @Column(name = "file_size_kb")
  private Integer fileSizeKb;

  @Column(name = "mime_type", length = 100)
  private String mimeType;

  @Column(name = "views_count")
  private Integer viewsCount = 0;

  @Column(name = "clicks_count")
  private Integer clicksCount = 0;

  @Column(name = "conversions_count")
  private Integer conversionsCount = 0;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 30)
  private AssetStatus status;

  @Column(name = "is_primary_asset")
  private Boolean isPrimaryAsset = false;


}