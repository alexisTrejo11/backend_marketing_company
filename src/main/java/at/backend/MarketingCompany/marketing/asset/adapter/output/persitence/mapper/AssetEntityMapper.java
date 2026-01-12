package at.backend.MarketingCompany.marketing.asset.adapter.output.persitence.mapper;

import at.backend.MarketingCompany.marketing.asset.adapter.output.persitence.model.MarketingAssetEntity;
import at.backend.MarketingCompany.marketing.asset.core.domain.entity.MarketingAsset;
import at.backend.MarketingCompany.marketing.asset.core.domain.entity.MarketingAssetReconstructParams;
import at.backend.MarketingCompany.marketing.asset.core.domain.valueobject.MarketingAssetId;
import at.backend.MarketingCompany.marketing.campaign.adapter.output.persistence.entity.MarketingCampaignEntity;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import org.springframework.stereotype.Component;

@Component
public class AssetEntityMapper {

	public MarketingAsset toDomain(MarketingAssetEntity entity) {
		if (entity == null) {
			return null;
		}

		var params = MarketingAssetReconstructParams.builder()
				.id(entity.getId() != null ? new MarketingAssetId(entity.getId()) : null)
				.campaignId(entity.getCampaign() != null
						? new MarketingCampaignId(entity.getCampaign().getId())
						: null)
				.assetType(entity.getAssetType())
				.name(entity.getName())
				.description(entity.getDescription())
				.status(entity.getStatus())
				.url(entity.getUrl())
				.fileSizeKb(entity.getFileSizeKb())
				.mimeType(entity.getMimeType())
				.viewsCount(entity.getViewsCount())
				.clicksCount(entity.getClicksCount())
				.conversionsCount(entity.getConversionsCount())
				.isPrimaryAsset(entity.getIsPrimaryAsset())
				.createdAt(entity.getCreatedAt())
				.updatedAt(entity.getUpdatedAt())
				.deletedAt(entity.getDeletedAt())
				.version(entity.getVersion())
				.build();

		return MarketingAsset.reconstruct(params);
	}


	public MarketingAssetEntity toEntity(MarketingAsset domain) {
		if (domain == null) {
			return null;
		}

		var entity = new MarketingAssetEntity();
		if (domain.getCampaignId() != null) {
			MarketingCampaignEntity campaignEntity = new MarketingCampaignEntity();
			campaignEntity.setId(domain.getCampaignId().getValue());
		}

		entity.setId(domain.getId() != null ? domain.getId().getValue() : null);
		entity.setAssetType(domain.getAssetType());
		entity.setName(domain.getName());
		entity.setDescription(domain.getDescription());
		entity.setStatus(domain.getStatus());
		entity.setUrl(domain.getUrl());
		entity.setFileSizeKb(domain.getFileSizeKb());
		entity.setMimeType(domain.getMimeType());
		entity.setViewsCount(domain.getViewsCount());
		entity.setClicksCount(domain.getClicksCount());
		entity.setConversionsCount(domain.getConversionsCount());
		entity.setIsPrimaryAsset(domain.isPrimaryAsset());
		entity.setCreatedAt(domain.getCreatedAt());
		entity.setUpdatedAt(domain.getUpdatedAt());
		entity.setDeletedAt(domain.getDeletedAt());
		entity.setVersion(domain.getVersion());

		return entity;
	}
}

