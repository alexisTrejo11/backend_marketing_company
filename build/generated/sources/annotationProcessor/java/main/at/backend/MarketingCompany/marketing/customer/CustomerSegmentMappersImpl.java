package at.backend.MarketingCompany.marketing.customer;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-27T17:36:06-0600",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.13.jar, environment: Java 23.0.2 (Homebrew)"
)
@Component
public class CustomerSegmentMappersImpl implements CustomerSegmentMappers {

    @Override
    public CustomerSegment inputToEntity(CustomerSegmentInsertDTO input) {
        if ( input == null ) {
            return null;
        }

        CustomerSegment customerSegment = new CustomerSegment();

        customerSegment.setName( input.getName() );
        customerSegment.setDescription( input.getDescription() );
        customerSegment.setSegmentCriteria( input.getSegmentCriteria() );
        customerSegment.setDynamic( input.isDynamic() );
        Map<String, String> map = input.getRules();
        if ( map != null ) {
            customerSegment.setRules( new LinkedHashMap<String, String>( map ) );
        }

        return customerSegment;
    }

    @Override
    public CustomerSegmentDTO entityToDTO(CustomerSegment entity) {
        if ( entity == null ) {
            return null;
        }

        CustomerSegmentDTO customerSegmentDTO = new CustomerSegmentDTO();

        customerSegmentDTO.setId( entity.getId() );
        customerSegmentDTO.setName( entity.getName() );
        customerSegmentDTO.setDescription( entity.getDescription() );
        customerSegmentDTO.setSegmentCriteria( entity.getSegmentCriteria() );
        customerSegmentDTO.setDynamic( entity.isDynamic() );
        Map<String, String> map = entity.getRules();
        if ( map != null ) {
            customerSegmentDTO.setRules( new LinkedHashMap<String, String>( map ) );
        }
        customerSegmentDTO.setLastUpdated( entity.getLastUpdated() );

        return customerSegmentDTO;
    }

    @Override
    public void updateEntity(CustomerSegment entity, CustomerSegmentInsertDTO input) {
        if ( input == null ) {
            return;
        }

        entity.setName( input.getName() );
        entity.setDescription( input.getDescription() );
        entity.setSegmentCriteria( input.getSegmentCriteria() );
        entity.setDynamic( input.isDynamic() );
        if ( entity.getRules() != null ) {
            Map<String, String> map = input.getRules();
            if ( map != null ) {
                entity.getRules().clear();
                entity.getRules().putAll( map );
            }
            else {
                entity.setRules( null );
            }
        }
        else {
            Map<String, String> map = input.getRules();
            if ( map != null ) {
                entity.setRules( new LinkedHashMap<String, String>( map ) );
            }
        }
    }
}
