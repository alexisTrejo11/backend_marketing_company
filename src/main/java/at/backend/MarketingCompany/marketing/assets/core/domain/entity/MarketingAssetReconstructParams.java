package at.backend.MarketingCompany.marketing.assets.core.domain.entity;

import at.backend.MarketingCompany.marketing.assets.core.domain.valueobject.MarketingAssetId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;

import java.time.LocalDateTime;

public record MarketingAssetReconstructParams(
    MarketingAssetId id,
    MarketingCampaignId campaignId,
    AssetType assetType,
    String name,
    String description,
    String url,
    String version,
    Integer fileSizeKb,
    String mimeType,
    Integer viewsCount,
    Integer clicksCount,
    Integer conversionsCount,
    AssetStatus status,
    Boolean isPrimaryAsset,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime deletedAt,
    Integer versionNumber
) {}