package at.backend.MarketingCompany.marketing.asset.core.port.input;

import at.backend.MarketingCompany.marketing.asset.core.application.command.CreateAssetCommand;
import at.backend.MarketingCompany.marketing.asset.core.application.command.UpdateAssetCommand;
import at.backend.MarketingCompany.marketing.asset.core.application.command.UpdateAssetPerformanceCommand;
import at.backend.MarketingCompany.marketing.asset.core.application.dto.AssetStatistics;
import at.backend.MarketingCompany.marketing.asset.core.application.query.AssetQuery;
import at.backend.MarketingCompany.marketing.asset.core.domain.entity.AssetStatus;
import at.backend.MarketingCompany.marketing.asset.core.domain.entity.AssetType;
import at.backend.MarketingCompany.marketing.asset.core.domain.entity.MarketingAsset;
import at.backend.MarketingCompany.marketing.asset.core.domain.valueobject.MarketingAssetId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AssetServicePort {
    
    // Command operations
    MarketingAsset createAsset(CreateAssetCommand command);
    MarketingAsset updateAsset(UpdateAssetCommand command);
    MarketingAsset updateAssetPerformance(UpdateAssetPerformanceCommand command);
    MarketingAsset markAsPrimary(MarketingAssetId assetId);
    MarketingAsset activateAsset(MarketingAssetId assetId);
    MarketingAsset archiveAsset(MarketingAssetId assetId);
    void deleteAsset(MarketingAssetId assetId);
    
    // Query operations
    MarketingAsset getAssetById(MarketingAssetId assetId);
    Page<MarketingAsset> searchAssets(AssetQuery query, Pageable pageable);
    Page<MarketingAsset> getAssetsByCampaign(MarketingCampaignId campaignId, Pageable pageable);
    Page<MarketingAsset> getAssetsByCampaignAndType(
        MarketingCampaignId campaignId, 
        AssetType assetType,
        Pageable pageable
    );
    Page<MarketingAsset> getAssetsByStatus(AssetStatus status, Pageable pageable);
    Page<MarketingAsset> getPrimaryAssetsByCampaign(
        MarketingCampaignId campaignId, 
        Pageable pageable
    );
    
    // Analytics operations
    AssetStatistics getAssetStatistics(MarketingCampaignId campaignId);
    Long getTotalAssetsByCampaign(MarketingCampaignId campaignId);
    Long getTotalClicksByCampaign(MarketingCampaignId campaignId);
    Double getAverageConversionRate(MarketingCampaignId campaignId);
}