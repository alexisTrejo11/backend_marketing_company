package at.backend.MarketingCompany.marketing.asset.adapter.input.graphql.dto;

import at.backend.MarketingCompany.marketing.asset.core.application.query.AssetQuery;
import at.backend.MarketingCompany.marketing.asset.core.domain.entity.AssetStatus;
import at.backend.MarketingCompany.marketing.asset.core.domain.entity.AssetType;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;

import java.util.List;

/**
 * Filter input for searching marketing assets
 */
public record AssetFilterInput(
    Long campaignId,
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
    public AssetQuery toQuery() {
        return new AssetQuery(
            campaignId != null ? new MarketingCampaignId(campaignId) : null,
            assetTypes,
            statuses,
            isPrimaryAsset,
            minViewsCount,
            maxViewsCount,
            minClicksCount,
            maxClicksCount,
            minConversionRate,
            maxConversionRate,
            searchTerm
        );
    }
}