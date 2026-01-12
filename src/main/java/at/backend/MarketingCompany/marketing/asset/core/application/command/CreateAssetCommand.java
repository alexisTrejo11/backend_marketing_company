package at.backend.MarketingCompany.marketing.asset.core.application.command;

import at.backend.MarketingCompany.marketing.asset.core.domain.entity.AssetType;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;

/**
 * Command to create a new marketing asset
 */
public record CreateAssetCommand(
    MarketingCampaignId campaignId,
    AssetType assetType,
    String name,
    String description,
    String url,
    String version,
    Integer fileSizeKb,
    String mimeType
) {}