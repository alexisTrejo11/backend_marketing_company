package at.backend.MarketingCompany.marketing.interaction.infrastructure.autoMappers;

import at.backend.MarketingCompany.marketing.campaign.infrastructure.autoMappers.CampaignMappers;
import at.backend.MarketingCompany.marketing.interaction.api.repository.CampaignInteractionModel;
import at.backend.MarketingCompany.marketing.interaction.domain.*;
import at.backend.MarketingCompany.marketing.interaction.infrastructure.DTOs.CampaignInteractionDTO;
import at.backend.MarketingCompany.marketing.interaction.infrastructure.DTOs.CampaignInteractionInsertDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InteractionMappers {

    private final CampaignMappers campaignMappers;

    public CampaignInteraction insertDTOToDomain(CampaignInteractionInsertDTO dto) {
        return CampaignInteraction.builder()
                .id(CampaignInteractionId.generate())
                //.campaign(campaignMappers.modelToDomain(dto.getCampaign()))
                //.customerModel(dto.getCustomerModel())
                .interactionType(dto.getInteractionType())
                .interactionDate(dto.getInteractionDate())
                .source(InteractionSource.builder()
                        .channel(dto.getSourceChannel())
                        .medium(dto.getSourceMedium())
                        .campaignName(dto.getSourceCampaign())
                        .build())
                .deviceInfo(DeviceInfo.builder()
                        .deviceType(dto.getDeviceType())
                        .ipAddress(dto.getIpAddress())
                        .build())
                .geoLocation(parseGeoLocation(dto.getGeoLocation()))
                .properties(new HashMap<>(dto.getProperties()))
                .details(dto.getDetails())
                .createdAt(LocalDateTime.now())
                .build();
    }

    // === Domain -> DTO ===
    public CampaignInteractionDTO domainToDTO(CampaignInteraction domain) {
        return CampaignInteractionDTO.builder()
                .id(domain.getId().value())
                .campaignId(domain.getCampaign().getId().getValue())
                .customerId(domain.getCustomerModel().getId())
                .interactionType(domain.getInteractionType())
                .interactionDate(domain.getInteractionDate())
                .sourceChannel(domain.getSource().getChannel())
                .sourceMedium(domain.getSource().getMedium())
                .sourceCampaign(domain.getSource().getCampaignName())
                .deviceType(domain.getDeviceInfo().getDeviceType())
                .ipAddress(domain.getDeviceInfo().getIpAddress())
                .geoLocation(formatGeoLocation(domain.getGeoLocation()))
                .properties(new HashMap<>(domain.getProperties()))
                .details(domain.getDetails())
                .resultedDealId(domain.getResultedDealEntity() != null ?
                        UUID.fromString(domain.getResultedDealEntity().getId()) : null)
                .conversionValue(domain.getConversionValue())
                .build();
    }

    public CampaignInteractionModel domainToModel(CampaignInteraction domain) {
        return CampaignInteractionModel.builder()
                .id(domain.getId().value())
                .campaign(campaignMappers.domainToModel(domain.getCampaign()))
                .customer(domain.getCustomerModel())
                .interactionType(domain.getInteractionType())
                .interactionDate(domain.getInteractionDate())
                .sourceChannel(domain.getSource().getChannel())
                .sourceMedium(domain.getSource().getMedium())
                .sourceCampaign(domain.getSource().getCampaignName())
                .deviceType(domain.getDeviceInfo().getDeviceType())
                .ipAddress(domain.getDeviceInfo().getIpAddress())
                .geoLocation(formatGeoLocation(domain.getGeoLocation()))
                .properties(new HashMap<>(domain.getProperties()))
                .details(domain.getDetails())
                .resultedDealEntity(domain.getResultedDealEntity())
                .conversionValue(domain.getConversionValue())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }

    public CampaignInteraction modelToDomain(CampaignInteractionModel model) {
        return CampaignInteraction.builder()
                .id(CampaignInteractionId.of(model.getId()))
                .campaign(campaignMappers.modelToDomain(model.getCampaign()))
                .customerModel(model.getCustomer())
                .interactionType(model.getInteractionType())
                .interactionDate(model.getInteractionDate())
                .source(InteractionSource.builder()
                        .channel(model.getSourceChannel())
                        .medium(model.getSourceMedium())
                        .campaignName(model.getSourceCampaign())
                        .build())
                .deviceInfo(DeviceInfo.builder()
                        .deviceType(model.getDeviceType())
                        .ipAddress(model.getIpAddress())
                        .build())
                .geoLocation(parseGeoLocation(model.getGeoLocation()))
                .properties(new HashMap<>(model.getProperties()))
                .details(model.getDetails())
                .resultedDealEntity(model.getResultedDealEntity())
                .conversionValue(model.getConversionValue())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }

    private GeoLocation parseGeoLocation(String geoLocation) {
        if (geoLocation == null) return null;
        String[] parts = geoLocation.split(",");
        return GeoLocation.builder()
                .country(parts.length > 0 ? parts[0] : null)
                .region(parts.length > 1 ? parts[1] : null)
                .city(parts.length > 2 ? parts[2] : null)
                .build();
    }

    private String formatGeoLocation(GeoLocation geo) {
        if (geo == null) return null;
        return String.format("%s,%s,%s",
                geo.getCountry(),
                geo.getRegion(),
                geo.getCity());
    }

    public CampaignInteractionDTO modelToDTO(CampaignInteractionModel model) {
        return CampaignInteractionDTO.builder()
                .id(model.getId())
                .campaignId(model.getCampaign().getId())
                .customerId(model.getCustomer().getId())
                .interactionType(model.getInteractionType())
                .interactionDate(model.getInteractionDate())
                .sourceChannel(model.getSourceChannel())
                .sourceMedium(model.getSourceMedium())
                .sourceCampaign(model.getSourceCampaign())
                .deviceType(model.getDeviceType())
                .ipAddress(model.getIpAddress())
                .geoLocation(formatGeoLocation(parseGeoLocation(model.getGeoLocation())))
                .properties(new HashMap<>(model.getProperties()))
                .details(model.getDetails())
                .resultedDealId(model.getResultedDealEntity() != null ? UUID.fromString(model.getResultedDealEntity().getId()) : null)
                .conversionValue(model.getConversionValue())
                .build();
    }
}
