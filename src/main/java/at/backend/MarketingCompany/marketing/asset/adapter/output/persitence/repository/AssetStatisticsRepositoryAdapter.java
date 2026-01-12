package at.backend.MarketingCompany.marketing.asset.adapter.output.persitence.repository;

import at.backend.MarketingCompany.marketing.asset.core.domain.entity.AssetStatus;
import at.backend.MarketingCompany.marketing.asset.core.port.output.AssetStatisticsRepositoryPort;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Repository
@RequiredArgsConstructor
public class AssetStatisticsRepositoryAdapter implements AssetStatisticsRepositoryPort {
	private final MarketingAssetJpaRepository jpaRepository;


	@Override
	public Map<String, Long> countByAssetTypeByCampaignId(MarketingCampaignId campaignId) {
		return Map.of();
	}

	@Override
	public Long sumConversionsByCampaignId(MarketingCampaignId campaignId) {
		return 0L;
	}

	@Override
	public Long countByCampaignIdAndStatus(MarketingCampaignId campaignId, AssetStatus status) {
		return 0L;
	}

	@Override
	public Long countPrimaryAssetsByCampaignId(MarketingCampaignId campaignId) {
		return 0L;
	}

	@Override
	public Long sumViewsByCampaignId(MarketingCampaignId campaignId) {
		return 0L;
	}

	@Override
	@Transactional(readOnly = true)
	public long countByCampaignId(MarketingCampaignId campaignId) {
		return jpaRepository.countByCampaignId(campaignId.getValue());
	}

	@Override
	@Transactional(readOnly = true)
	public long sumClicksByCampaignId(MarketingCampaignId campaignId) {
		return jpaRepository.sumClicksByCampaignId(campaignId.getValue());
	}

}
