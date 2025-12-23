package at.backend.MarketingCompany.marketing.interaction.adapter.output.persitence.mapper;

import at.backend.MarketingCompany.marketing.interaction.adapter.output.persitence.model.CampaignInteractionEntity;
import at.backend.MarketingCompany.marketing.interaction.core.domain.entity.CampaignInteraction;
import at.backend.MarketingCompany.marketing.interaction.core.domain.entity.CampaignInteractionReconstructParams;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.CampaignInteractionId;
import org.springframework.stereotype.Component;

//TODO: Implement mapping for related entities (Campaign, Customer, Channel) when their mappers are available
@Component
public class MarketingInteractionEntityMapper {
	public CampaignInteractionEntity toEntity(CampaignInteraction interaction) {
		if (interaction == null) {
			return null;
		}
		CampaignInteractionEntity entity = new CampaignInteractionEntity();
		entity.setId(interaction.getId() != null ? interaction.getId().getValue() : null);
		entity.setCampaign(null);
		entity.setCustomer(null); // Mapping for customer entity should be implemented
		entity.setMarketingInteractionType(interaction.getMarketingInteractionType());
		entity.setInteractionDate(interaction.getInteractionDate());
		entity.setChannel(null); // Mapping for channel entity should be implemented
		entity.setUtmSource(interaction.getUtmSource());
		entity.setUtmMedium(interaction.getUtmMedium());
		entity.setUtmCampaign(interaction.getUtmCampaign());
		entity.setUtmContent(interaction.getUtmContent());
		entity.setUtmTerm(interaction.getUtmTerm());
		entity.setDeviceType(interaction.getDeviceType());
		entity.setDeviceOs(interaction.getDeviceOs());
		entity.setBrowser(interaction.getBrowser());
		entity.setCountryCode(interaction.getCountryCode());
		entity.setCity(interaction.getCity());
		entity.setReferrerUrl(interaction.getReferrerUrl());
		entity.setConversionValue(interaction.getConversionValue());
		entity.setLandingPageUrl(interaction.getLandingPageUrl());
		entity.setDealId(interaction.getDealId());
		entity.setSessionId(interaction.getSessionId());
		entity.setIsConversion(interaction.isConversion());
		entity.setProperties(interaction.getProperties());
		entity.setCreatedAt(interaction.getCreatedAt());
		entity.setUpdatedAt(interaction.getUpdatedAt());
		entity.setDeletedAt(interaction.getDeletedAt());
		entity.setVersion(interaction.getVersion());
		return entity;
	}

	public CampaignInteraction toDomain(CampaignInteractionEntity entity) {
		if (entity == null) {
			return null;
		}

		var params = CampaignInteractionReconstructParams.builder()
				.id(entity.getId() != null ? new CampaignInteractionId(entity.getId()) : null)
				.campaignId(null) // Mapping for campaign ID should be implemented
				.customerId(null) // Mapping for customer ID should be implemented
				.marketingInteractionType(entity.getMarketingInteractionType())
				.interactionDate(entity.getInteractionDate())
				.channelId(null) // Mapping for channel ID should be implemented
				.utmSource(entity.getUtmSource())
				.utmMedium(entity.getUtmMedium())
				.utmCampaign(entity.getUtmCampaign())
				.utmContent(entity.getUtmContent())
				.utmTerm(entity.getUtmTerm())
				.deviceType(entity.getDeviceType())
				.deviceOs(entity.getDeviceOs())
				.browser(entity.getBrowser())
				.countryCode(entity.getCountryCode())
				.city(entity.getCity())
				.dealId(entity.getDealId())
				.conversionValue(entity.getConversionValue())
				.isConversion(entity.getIsConversion())
				.landingPageUrl(entity.getLandingPageUrl())
				.referrerUrl(entity.getReferrerUrl())
				.sessionId(entity.getSessionId())
				.properties(entity.getProperties())
				.createdAt(entity.getCreatedAt())
				.updatedAt(entity.getUpdatedAt())
				.deletedAt(entity.getDeletedAt())
				.version(entity.getVersion())
				.build();

		return CampaignInteraction.reconstruct(params);
	}

}
