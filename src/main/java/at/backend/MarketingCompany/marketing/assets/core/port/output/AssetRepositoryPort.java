package at.backend.MarketingCompany.marketing.assets.core.port.output;

import at.backend.MarketingCompany.marketing.assets.core.domain.entity.AssetStatus;
import at.backend.MarketingCompany.marketing.assets.core.domain.entity.AssetType;
import at.backend.MarketingCompany.marketing.assets.core.domain.entity.MarketingAsset;
import at.backend.MarketingCompany.marketing.assets.core.domain.valueobject.MarketingAssetId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AssetRepositoryPort {
    
    MarketingAsset save(MarketingAsset asset);
    
    Optional<MarketingAsset> findById(MarketingAssetId id);
    
    void delete(MarketingAssetId id);
    
    Page<MarketingAsset> findByCampaignId(MarketingCampaignId campaignId, Pageable pageable);
    
    Page<MarketingAsset> findByCampaignIdAndAssetType(
		    MarketingCampaignId campaignId,
            AssetType assetType,
            Pageable pageable);
    
    Page<MarketingAsset> findByStatus(
            AssetStatus status,
            Pageable pageable);
    
    Page<MarketingAsset> findPrimaryAssetsByCampaignId(MarketingCampaignId campaignId, Pageable pageable);
    
    Page<MarketingAsset> searchByName(String searchTerm, Pageable pageable);
    
    long countByCampaignId(MarketingCampaignId campaignId);
    
    long sumClicksByCampaignId(MarketingCampaignId campaignId);
}