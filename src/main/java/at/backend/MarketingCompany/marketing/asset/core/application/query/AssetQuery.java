package at.backend.MarketingCompany.marketing.asset.core.application.query;

import at.backend.MarketingCompany.marketing.asset.core.domain.entity.AssetStatus;
import at.backend.MarketingCompany.marketing.asset.core.domain.entity.AssetType;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;

import java.util.List;

/**
 * Query object for filtering marketing assets
 */
public record AssetQuery(
    MarketingCampaignId campaignId,
    List<AssetType> assetTypes,
    List<AssetStatus> statuses,
    Boolean isPrimaryAsset,
    Integer minViewsCount,
    Integer maxViewsCount,
    Integer minClicksCount,
    Integer maxClicksCount,
    Double minConversionRate,
    Double maxConversionRate,
    String searchTerm
) {
    public AssetQuery() {
        this(null, null, null, null, null, null, null, null, null, null, null);
    }
    
    public boolean isEmpty() {
        return campaignId == null &&
               (assetTypes == null || assetTypes.isEmpty()) &&
               (statuses == null || statuses.isEmpty()) &&
               isPrimaryAsset == null &&
               minViewsCount == null &&
               maxViewsCount == null &&
               minClicksCount == null &&
               maxClicksCount == null &&
               minConversionRate == null &&
               maxConversionRate == null &&
               (searchTerm == null || searchTerm.isBlank());
    }
}