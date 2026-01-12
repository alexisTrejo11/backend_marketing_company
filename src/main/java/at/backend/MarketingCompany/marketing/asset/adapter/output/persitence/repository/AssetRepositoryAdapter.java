package at.backend.MarketingCompany.marketing.asset.adapter.output.persitence.repository;

import at.backend.MarketingCompany.marketing.asset.adapter.output.persitence.mapper.AssetEntityMapper;
import at.backend.MarketingCompany.marketing.asset.adapter.output.persitence.model.MarketingAssetEntity;
import at.backend.MarketingCompany.marketing.asset.core.application.query.AssetQuery;
import at.backend.MarketingCompany.marketing.asset.core.domain.entity.AssetStatus;
import at.backend.MarketingCompany.marketing.asset.core.domain.entity.AssetType;
import at.backend.MarketingCompany.marketing.asset.core.domain.entity.MarketingAsset;
import at.backend.MarketingCompany.marketing.asset.core.domain.valueobject.MarketingAssetId;
import at.backend.MarketingCompany.marketing.asset.core.port.output.AssetRepositoryPort;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AssetRepositoryAdapter implements AssetRepositoryPort {
	private final MarketingAssetJpaRepository jpaRepository;
	private final AssetEntityMapper mapper;

	@Override
	public Page<MarketingAsset> findAll(Pageable pageable) {
		return null;
	}

	@Override
	public Page<MarketingAsset> findByFilters(AssetQuery query, Pageable pageable) {
		return null;
	}

	@Override
	@Transactional
	public MarketingAsset save(MarketingAsset asset) {
		MarketingAssetEntity entity = mapper.toEntity(asset);
		MarketingAssetEntity savedEntity = jpaRepository.save(entity);
		return mapper.toDomain(savedEntity);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<MarketingAsset> findById(MarketingAssetId id) {
		return jpaRepository.findByIdAndNotDeleted(id.getValue())
				.map(mapper::toDomain);
	}

	@Override
	@Transactional
	public void delete(MarketingAssetId id) {
		jpaRepository.deleteById(id.getValue());
	}

	@Override
	@Transactional(readOnly = true)
	public Page<MarketingAsset> findByCampaignId(MarketingCampaignId campaignId, Pageable pageable) {
		return jpaRepository.findByCampaignId(campaignId.getValue(), pageable)
				.map(mapper::toDomain);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<MarketingAsset> findByCampaignIdAndAssetType(
			MarketingCampaignId campaignId,
			AssetType assetType,
			Pageable pageable) {
		return jpaRepository.findByCampaignIdAndAssetType(campaignId.getValue(), assetType, pageable)
				.map(mapper::toDomain);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<MarketingAsset> findByStatus(
			AssetStatus status,
			Pageable pageable) {
		return jpaRepository.findByStatus(status, pageable)
				.map(mapper::toDomain);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<MarketingAsset> findPrimaryAssetsByCampaignId(MarketingCampaignId campaignId, Pageable pageable) {
		return jpaRepository.findPrimaryAssetsByCampaignId(campaignId.getValue(), pageable)
				.map(mapper::toDomain);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<MarketingAsset> searchByName(String searchTerm, Pageable pageable) {
		return jpaRepository.searchByName(searchTerm, pageable)
				.map(mapper::toDomain);
	}

	@Override
	public List<MarketingAsset> findTopAssetsByViews(MarketingCampaignId campaignId, int topN) {
		return List.of();
	}

	@Override
	public List<MarketingAsset> findTopAssetsByClicks(MarketingCampaignId campaignId, int topN) {
		return List.of();
	}

	@Override
	public List<MarketingAsset> findTopAssetsByConversions(MarketingCampaignId campaignId, int topN) {
		return List.of();
	}


}