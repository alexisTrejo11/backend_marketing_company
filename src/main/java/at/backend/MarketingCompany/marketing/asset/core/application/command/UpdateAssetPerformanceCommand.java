package at.backend.MarketingCompany.marketing.asset.core.application.command;

import at.backend.MarketingCompany.marketing.asset.core.domain.valueobject.MarketingAssetId;

/**
 * Command to update asset performance metrics
 */
public record UpdateAssetPerformanceCommand(
    MarketingAssetId assetId,
    Integer viewsCount,
    Integer clicksCount,
    Integer conversionsCount
) {}