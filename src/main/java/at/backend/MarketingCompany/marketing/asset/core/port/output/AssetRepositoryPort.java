package at.backend.MarketingCompany.marketing.asset.core.port.output;

import at.backend.MarketingCompany.marketing.asset.core.application.query.AssetQuery;
import at.backend.MarketingCompany.marketing.asset.core.domain.entity.AssetStatus;
import at.backend.MarketingCompany.marketing.asset.core.domain.entity.AssetType;
import at.backend.MarketingCompany.marketing.asset.core.domain.entity.MarketingAsset;
import at.backend.MarketingCompany.marketing.asset.core.domain.valueobject.MarketingAssetId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AssetRepositoryPort {
	Page<MarketingAsset> findAll(Pageable pageable);

	Page<MarketingAsset> findByFilters(AssetQuery query, Pageable pageable);

	MarketingAsset save(MarketingAsset asset);

	Optional<MarketingAsset> findById(MarketingAssetId id);

	void delete(MarketingAssetId id);

	Page<MarketingAsset> findByCampaignId(MarketingCampaignId campaignId, Pageable pageable);

	Page<MarketingAsset> findByCampaignIdAndAssetType(MarketingCampaignId campaignId, AssetType assetType, Pageable pageable);

	Page<MarketingAsset> findByStatus(AssetStatus status, Pageable pageable);

	Page<MarketingAsset> findPrimaryAssetsByCampaignId(MarketingCampaignId campaignId, Pageable pageable);

	Page<MarketingAsset> searchByName(String searchTerm, Pageable pageable);

	List<MarketingAsset> findTopAssetsByViews(MarketingCampaignId campaignId, int topN);

	List<MarketingAsset> findTopAssetsByClicks(MarketingCampaignId campaignId, int topN);

	List<MarketingAsset> findTopAssetsByConversions(MarketingCampaignId campaignId, int topN);
}