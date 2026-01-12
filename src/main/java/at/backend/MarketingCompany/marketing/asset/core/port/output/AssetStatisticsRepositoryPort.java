package at.backend.MarketingCompany.marketing.asset.core.port.output;

import at.backend.MarketingCompany.marketing.asset.core.domain.entity.AssetStatus;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;

import java.util.Map;

public interface AssetStatisticsRepositoryPort {

	Map<String, Long> countByAssetTypeByCampaignId(MarketingCampaignId campaignId);

	Long sumConversionsByCampaignId(MarketingCampaignId campaignId);

	Long countByCampaignIdAndStatus(MarketingCampaignId campaignId, AssetStatus status);

	Long countPrimaryAssetsByCampaignId(MarketingCampaignId campaignId);

	Long sumViewsByCampaignId(MarketingCampaignId campaignId);

	long countByCampaignId(MarketingCampaignId campaignId);

	long sumClicksByCampaignId(MarketingCampaignId campaignId);
}
