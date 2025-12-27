package at.backend.MarketingCompany.marketing.asset.core.application.command;

import at.backend.MarketingCompany.marketing.asset.core.domain.entity.AssetType;
import at.backend.MarketingCompany.marketing.asset.core.domain.valueobject.MarketingAssetId;
import lombok.Builder;

/**
 * Command to update an existing marketing asset
 */
@Builder
public record UpdateAssetCommand(
    MarketingAssetId assetId,
    AssetType assetType,
    String name,
    String description,
    String url,
    Integer fileSizeKb,
    String mimeType
) {}