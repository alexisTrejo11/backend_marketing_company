package at.backend.MarketingCompany.marketing.interaction.infrastructure.autoMappers;

import at.backend.MarketingCompany.customer.api.repository.CustomerModel;
import at.backend.MarketingCompany.marketing.campaign.api.repository.MarketingCampaignModel;
import at.backend.MarketingCompany.marketing.interaction.api.repository.CampaignInteractionModel;
import at.backend.MarketingCompany.marketing.interaction.infrastructure.DTOs.CampaignInteractionDTO;
import at.backend.MarketingCompany.marketing.interaction.infrastructure.DTOs.CampaignInteractionInsertDTO;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-26T13:53:26-0600",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.11.1.jar, environment: Java 23.0.2 (Homebrew)"
)
@Component
public class CampaignInteractionMappersImpl implements CampaignInteractionMappers {

    @Override
    public CampaignInteractionModel inputToEntity(CampaignInteractionInsertDTO input) {
        if ( input == null ) {
            return null;
        }

        CampaignInteractionModel.CampaignInteractionModelBuilder campaignInteractionModel = CampaignInteractionModel.builder();

        campaignInteractionModel.interactionType( input.getInteractionType() );
        campaignInteractionModel.interactionDate( input.getInteractionDate() );
        campaignInteractionModel.sourceChannel( input.getSourceChannel() );
        campaignInteractionModel.sourceMedium( input.getSourceMedium() );
        campaignInteractionModel.sourceCampaign( input.getSourceCampaign() );
        campaignInteractionModel.deviceType( input.getDeviceType() );
        campaignInteractionModel.ipAddress( input.getIpAddress() );
        campaignInteractionModel.geoLocation( input.getGeoLocation() );
        Map<String, String> map = input.getProperties();
        if ( map != null ) {
            campaignInteractionModel.properties( new LinkedHashMap<String, String>( map ) );
        }
        campaignInteractionModel.details( input.getDetails() );
        campaignInteractionModel.conversionValue( input.getConversionValue() );

        return campaignInteractionModel.build();
    }

    @Override
    public CampaignInteractionDTO entityToDTO(CampaignInteractionModel entity) {
        if ( entity == null ) {
            return null;
        }

        CampaignInteractionDTO.CampaignInteractionDTOBuilder campaignInteractionDTO = CampaignInteractionDTO.builder();

        campaignInteractionDTO.campaignId( entityCampaignId( entity ) );
        campaignInteractionDTO.customerId( entityCustomerId( entity ) );
        campaignInteractionDTO.id( entity.getId() );
        campaignInteractionDTO.interactionType( entity.getInteractionType() );
        campaignInteractionDTO.interactionDate( entity.getInteractionDate() );
        campaignInteractionDTO.sourceChannel( entity.getSourceChannel() );
        campaignInteractionDTO.sourceMedium( entity.getSourceMedium() );
        campaignInteractionDTO.sourceCampaign( entity.getSourceCampaign() );
        campaignInteractionDTO.deviceType( entity.getDeviceType() );
        campaignInteractionDTO.ipAddress( entity.getIpAddress() );
        campaignInteractionDTO.geoLocation( entity.getGeoLocation() );
        Map<String, String> map = entity.getProperties();
        if ( map != null ) {
            campaignInteractionDTO.properties( new LinkedHashMap<String, String>( map ) );
        }
        campaignInteractionDTO.details( entity.getDetails() );
        campaignInteractionDTO.conversionValue( entity.getConversionValue() );

        return campaignInteractionDTO.build();
    }

    @Override
    public void updateEntity(CampaignInteractionModel entity, CampaignInteractionInsertDTO input) {
        if ( input == null ) {
            return;
        }

        entity.setInteractionType( input.getInteractionType() );
        entity.setInteractionDate( input.getInteractionDate() );
        entity.setSourceChannel( input.getSourceChannel() );
        entity.setSourceMedium( input.getSourceMedium() );
        entity.setSourceCampaign( input.getSourceCampaign() );
        entity.setDeviceType( input.getDeviceType() );
        entity.setIpAddress( input.getIpAddress() );
        entity.setGeoLocation( input.getGeoLocation() );
        if ( entity.getProperties() != null ) {
            Map<String, String> map = input.getProperties();
            if ( map != null ) {
                entity.getProperties().clear();
                entity.getProperties().putAll( map );
            }
            else {
                entity.setProperties( null );
            }
        }
        else {
            Map<String, String> map = input.getProperties();
            if ( map != null ) {
                entity.setProperties( new LinkedHashMap<String, String>( map ) );
            }
        }
        entity.setDetails( input.getDetails() );
        entity.setConversionValue( input.getConversionValue() );
    }

    private UUID entityCampaignId(CampaignInteractionModel campaignInteractionModel) {
        if ( campaignInteractionModel == null ) {
            return null;
        }
        MarketingCampaignModel campaign = campaignInteractionModel.getCampaign();
        if ( campaign == null ) {
            return null;
        }
        UUID id = campaign.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID entityCustomerId(CampaignInteractionModel campaignInteractionModel) {
        if ( campaignInteractionModel == null ) {
            return null;
        }
        CustomerModel customer = campaignInteractionModel.getCustomer();
        if ( customer == null ) {
            return null;
        }
        UUID id = customer.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
