package at.backend.MarketingCompany.marketing.assets.core.domain.entity;

import at.backend.MarketingCompany.marketing.assets.core.domain.valueobject.MarketingAssetId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.exception.MarketingDomainException;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.shared.domain.BaseDomainEntity;
import lombok.Getter;

@Getter
public class MarketingAsset extends BaseDomainEntity<MarketingAssetId> {
  private MarketingCampaignId campaignId;
  private AssetType assetType;
  private String name;
  private String description;
  private String url;
  private String version;
  private Integer fileSizeKb;
  private String mimeType;
  private int viewsCount;
  private int clicksCount;
  private int conversionsCount;
  private AssetStatus status;
  private boolean isPrimaryAsset;

  private MarketingAsset() {
    this.version = "1.0";
    this.viewsCount = 0;
    this.clicksCount = 0;
    this.conversionsCount = 0;
    this.status = AssetStatus.DRAFT;
    this.isPrimaryAsset = false;
  }

  public MarketingAsset(MarketingAssetId id) {
    super(id);
    this.version = "1.0";
    this.viewsCount = 0;
    this.clicksCount = 0;
    this.conversionsCount = 0;
    this.status = AssetStatus.DRAFT;
    this.isPrimaryAsset = false;
  }

  public static MarketingAsset create(
      MarketingCampaignId campaignId,
      AssetType assetType,
      String name,
      String url) {
    
    if (campaignId == null) {
      throw new MarketingDomainException("Campaign ID is required");
    }
    if (assetType == null) {
      throw new MarketingDomainException("Asset type is required");
    }
    if (name == null || name.isBlank()) {
      throw new MarketingDomainException("Asset name is required");
    }
    if (url == null || url.isBlank()) {
      throw new MarketingDomainException("Asset URL is required");
    }

    MarketingAsset asset = new MarketingAsset(MarketingAssetId.generate());
    asset.campaignId = campaignId;
    asset.assetType = assetType;
    asset.name = name;
    asset.url = url;
    asset.status = AssetStatus.DRAFT;

    return asset;
  }

  public static MarketingAsset reconstruct(MarketingAssetReconstructParams params) {
    if (params == null) {
      return null;
    }

    MarketingAsset asset = new MarketingAsset();
    asset.id = params.id();
    asset.campaignId = params.campaignId();
    asset.assetType = params.assetType();
    asset.name = params.name();
    asset.description = params.description();
    asset.url = params.url();
    asset.version = params.version() != null ? params.version() : "1.0";
    asset.fileSizeKb = params.fileSizeKb();
    asset.mimeType = params.mimeType();
    asset.viewsCount = params.viewsCount() != null ? params.viewsCount() : 0;
    asset.clicksCount = params.clicksCount() != null ? params.clicksCount() : 0;
    asset.conversionsCount = params.conversionsCount() != null ? params.conversionsCount() : 0;
    asset.status = params.status() != null ? params.status() : AssetStatus.DRAFT;
    asset.isPrimaryAsset = params.isPrimaryAsset() != null ? params.isPrimaryAsset() : false;
    asset.createdAt = params.createdAt();
    asset.updatedAt = params.updatedAt();
    asset.deletedAt = params.deletedAt();
    asset.version = params.version();

    return asset;
  }

  public void markAsReady() {
    if (status == AssetStatus.ARCHIVED) {
      throw new MarketingDomainException("Cannot mark archived asset as ready");
    }
    this.status = AssetStatus.READY;
  }

  public void activate() {
    if (status != AssetStatus.READY) {
      throw new MarketingDomainException("Asset must be ready before activation");
    }
    this.status = AssetStatus.ACTIVE;
  }

  public void archive() {
    this.status = AssetStatus.ARCHIVED;
  }

  public void incrementViews() {
    this.viewsCount++;
  }

  public void incrementClicks() {
    this.clicksCount++;
  }

  public void incrementConversions() {
    this.conversionsCount++;
  }

  public double getConversionRate() {
    if (clicksCount == 0) {
      return 0.0;
    }
    return (double) conversionsCount / clicksCount * 100;
  }

}

