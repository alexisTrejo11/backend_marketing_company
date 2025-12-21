package at.backend.MarketingCompany.marketing.attribution.adapter.output.persitence.mapper;

import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.DealId;
import at.backend.MarketingCompany.marketing.attribution.adapter.output.persitence.model.CampaignAttributionEntity;
import at.backend.MarketingCompany.marketing.attribution.core.domain.entity.CampaignAttribution;
import at.backend.MarketingCompany.marketing.attribution.core.domain.entity.CampaignAttributionReconstructParams;
import at.backend.MarketingCompany.marketing.attribution.core.domain.valueobject.CampaignAttributionId;
import at.backend.MarketingCompany.marketing.campaign.adapter.output.persistence.entity.MarketingCampaignEntity;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import org.springframework.stereotype.Component;

@Component
public class AttributionEntityMapper {

	public CampaignAttributionEntity toEntity(CampaignAttribution attribution) {
		if (attribution == null) {
			return null;
		}
		var entity = new CampaignAttributionEntity();
		entity.setId(attribution.getId() != null ? attribution.getId().getValue() : null);
		entity.setCampaign(attribution.getCampaignId() != null ? new MarketingCampaignEntity(attribution.getCampaignId().getValue()) : null);
		entity.setAttributionModel(attribution.getAttributionModel());
		entity.setAttributionPercentage(attribution.getAttributionPercentage());
		entity.setAttributedRevenue(attribution.getAttributedRevenue());
		entity.setTouchTimestamps(attribution.getTouchTimestamps());
		entity.setTouchCount(attribution.getTouchCount());
		entity.setFirstTouchWeight(attribution.getFirstTouchWeight());
		entity.setLastTouchWeight(attribution.getLastTouchWeight());
		entity.setLinearWeight(attribution.getLinearWeight());
		entity.setDealId(attribution.getDealId() != null ? attribution.getDealId().getValue() : null);
		entity.setCreatedAt(attribution.getCreatedAt());
		entity.setUpdatedAt(attribution.getUpdatedAt());
		entity.setDeletedAt(attribution.getDeletedAt());
		entity.setVersion(attribution.getVersion());
		return entity;
	}

	public CampaignAttribution toDomain(CampaignAttributionEntity entity) {
		var params = CampaignAttributionReconstructParams.builder()
				.id(entity.getId() != null ? new CampaignAttributionId(entity.getId()) : null)
				.dealId(entity.getDealId() != null ? new DealId(entity.getDealId()) : null)
				.campaignId(entity.getCampaign() != null ? new MarketingCampaignId(entity.getCampaign().getId()) : null)
				.attributionModel(entity.getAttributionModel())
				.attributionPercentage(entity.getAttributionPercentage())
				.attributedRevenue(entity.getAttributedRevenue())
				.touchTimestamps(entity.getTouchTimestamps())
				.touchCount(entity.getTouchCount())
				.firstTouchWeight(entity.getFirstTouchWeight())
				.lastTouchWeight(entity.getLastTouchWeight())
				.linearWeight(entity.getLinearWeight())
				.createdAt(entity.getCreatedAt())
				.updatedAt(entity.getUpdatedAt())
				.deletedAt(entity.getDeletedAt())
				.version(entity.getVersion())
				.build();


		return CampaignAttribution.reconstruct(params);
	}
}
