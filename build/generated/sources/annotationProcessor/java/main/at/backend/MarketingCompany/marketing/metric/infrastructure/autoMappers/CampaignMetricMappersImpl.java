package at.backend.MarketingCompany.marketing.metric.infrastructure.autoMappers;

import at.backend.MarketingCompany.marketing.campaign.api.repository.MarketingCampaignModel;
import at.backend.MarketingCompany.marketing.metric.api.repository.CampaignMetricModel;
import at.backend.MarketingCompany.marketing.metric.infrastructure.DTOs.CampaignMetricDTO;
import at.backend.MarketingCompany.marketing.metric.infrastructure.DTOs.CampaignMetricInsertDTO;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-27T17:36:06-0600",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.13.jar, environment: Java 23.0.2 (Homebrew)"
)
@Component
public class CampaignMetricMappersImpl implements CampaignMetricMappers {

    @Override
    public CampaignMetricModel inputToEntity(CampaignMetricInsertDTO input) {
        if ( input == null ) {
            return null;
        }

        CampaignMetricModel.CampaignMetricModelBuilder campaignMetricModel = CampaignMetricModel.builder();

        campaignMetricModel.name( input.getName() );
        campaignMetricModel.description( input.getDescription() );
        campaignMetricModel.type( input.getType() );
        campaignMetricModel.value( input.getValue() );
        campaignMetricModel.targetValue( input.getTargetValue() );
        campaignMetricModel.measurementUnit( input.getMeasurementUnit() );
        campaignMetricModel.calculationFormula( input.getCalculationFormula() );
        campaignMetricModel.dataSource( input.getDataSource() );
        campaignMetricModel.automated( input.isAutomated() );

        return campaignMetricModel.build();
    }

    @Override
    public CampaignMetricDTO entityToDTO(CampaignMetricModel entity) {
        if ( entity == null ) {
            return null;
        }

        CampaignMetricDTO.CampaignMetricDTOBuilder campaignMetricDTO = CampaignMetricDTO.builder();

        campaignMetricDTO.campaignId( entityCampaignId( entity ) );
        campaignMetricDTO.id( entity.getId() );
        campaignMetricDTO.name( entity.getName() );
        campaignMetricDTO.description( entity.getDescription() );
        campaignMetricDTO.type( entity.getType() );
        campaignMetricDTO.value( entity.getValue() );
        campaignMetricDTO.targetValue( entity.getTargetValue() );
        campaignMetricDTO.measurementUnit( entity.getMeasurementUnit() );
        campaignMetricDTO.lastCalculated( entity.getLastCalculated() );
        campaignMetricDTO.calculationFormula( entity.getCalculationFormula() );
        campaignMetricDTO.dataSource( entity.getDataSource() );
        campaignMetricDTO.automated( entity.isAutomated() );

        return campaignMetricDTO.build();
    }

    @Override
    public void updateEntity(CampaignMetricModel entity, CampaignMetricInsertDTO input) {
        if ( input == null ) {
            return;
        }

        entity.setName( input.getName() );
        entity.setDescription( input.getDescription() );
        entity.setType( input.getType() );
        entity.setValue( input.getValue() );
        entity.setTargetValue( input.getTargetValue() );
        entity.setMeasurementUnit( input.getMeasurementUnit() );
        entity.setCalculationFormula( input.getCalculationFormula() );
        entity.setDataSource( input.getDataSource() );
        entity.setAutomated( input.isAutomated() );
    }

    private UUID entityCampaignId(CampaignMetricModel campaignMetricModel) {
        if ( campaignMetricModel == null ) {
            return null;
        }
        MarketingCampaignModel campaign = campaignMetricModel.getCampaign();
        if ( campaign == null ) {
            return null;
        }
        UUID id = campaign.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
