package at.backend.MarketingCompany.marketing.interaction.adapter.output.persitence.mapper;

import org.springframework.stereotype.Component;

import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.DealId;
import at.backend.MarketingCompany.customer.adapter.output.persistence.entity.CustomerCompanyEntity;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.marketing.campaign.adapter.output.persistence.entity.MarketingCampaignEntity;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.channel.adapter.output.persitence.model.MarketingChannelEntity;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.MarketingChannelId;
import at.backend.MarketingCompany.marketing.interaction.adapter.output.persitence.model.CampaignInteractionEntity;
import at.backend.MarketingCompany.marketing.interaction.core.domain.entity.CampaignInteraction;
import at.backend.MarketingCompany.marketing.interaction.core.domain.entity.CampaignInteractionReconstructParams;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.CampaignInteractionId;

@Component
public class MarketingInteractionEntityMapper {

  public CampaignInteractionEntity toEntity(CampaignInteraction interaction) {
    if (interaction == null) {
      return null;
    }

    CampaignInteractionEntity entity = new CampaignInteractionEntity();
    entity.setId(interaction.getId() != null ? interaction.getId().getValue() : null);
    entity.setMarketingInteractionType(interaction.getMarketingInteractionType());
    entity.setInteractionDate(interaction.getInteractionDate());
    entity.setUtmSource(interaction.getUtmParameters().getSource());
    entity.setUtmMedium(interaction.getUtmParameters().getMedium());
    entity.setUtmCampaign(interaction.getUtmParameters().getCampaign());
    entity.setUtmContent(interaction.getUtmParameters().getContent());
    entity.setUtmTerm(interaction.getUtmParameters().getTerm());
    entity.setDeviceType(interaction.getDeviceInfo().getType());
    entity.setDeviceOs(interaction.getDeviceInfo().getOs());
    entity.setBrowser(interaction.getDeviceInfo().getBrowser());
    entity.setCountryCode(interaction.getLocationInfo().getCountryCode());
    entity.setCity(interaction.getLocationInfo().getCity());
    entity.setReferrerUrl(interaction.getPageInfo().getReferrerUrl());
    entity.setConversionValue(interaction.getConversionValue());
    entity.setLandingPageUrl(interaction.getPageInfo().getLandingPageUrl());
    entity.setDealId(interaction.getDealId() != null ? interaction.getDealId().getValue() : null);
    entity.setSessionId(interaction.getSessionId());
    entity.setIsConversion(interaction.isConversion());
    entity.setProperties(interaction.getProperties());
    entity.setCreatedAt(interaction.getCreatedAt());
    entity.setConversionDate(interaction.getConversionDate());
    entity.setConversionNotes(interaction.getConversionNotes());
    entity.setUpdatedAt(interaction.getUpdatedAt());
    entity.setDeletedAt(interaction.getDeletedAt());
    entity.setVersion(interaction.getVersion());

    if (interaction.getCampaignId() != null) {
      var campaignEntity = new MarketingCampaignEntity(interaction.getCampaignId().getValue());
      entity.setCampaign(campaignEntity);
    }

    if (interaction.getCustomerId() != null) {
      var customerEntity = new CustomerCompanyEntity(interaction.getCustomerId().getValue());
      entity.setCustomer(customerEntity);
    }

    if (interaction.getChannelId() != null) {
      var channelEntity = new MarketingChannelEntity(interaction.getChannelId().getValue());
      entity.setChannel(channelEntity);
    }

    return entity;
  }

  public CampaignInteraction toDomain(CampaignInteractionEntity entity) {
    if (entity == null) {
      return null;
    }

    MarketingCampaignId campaignId = entity.getCampaign() != null
        ? new MarketingCampaignId(entity.getCampaign().getId())
        : null;

    CustomerCompanyId customerId = entity.getCustomer() != null
        ? new CustomerCompanyId(entity.getCustomer().getId())
        : null;

    MarketingChannelId channelId = entity.getChannel() != null
        ? new MarketingChannelId(entity.getChannel().getId())
        : null;

    var params = CampaignInteractionReconstructParams.builder()
        .id(entity.getId() != null ? new CampaignInteractionId(entity.getId()) : null)
        .campaignId(campaignId)
        .customerId(customerId)
        .marketingInteractionType(entity.getMarketingInteractionType())
        .interactionDate(entity.getInteractionDate())
        .channelId(channelId)
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
        .dealId(entity.getDealId() != null
            ? new DealId(entity.getDealId())
            : null)
        .conversionValue(entity.getConversionValue())
        .isConversion(entity.getIsConversion())
        .conversionDate(entity.getConversionDate())
        .conversionNotes(entity.getConversionNotes())
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
