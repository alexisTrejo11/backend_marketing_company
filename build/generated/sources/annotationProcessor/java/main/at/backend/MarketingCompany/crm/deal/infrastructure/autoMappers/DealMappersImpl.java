package at.backend.MarketingCompany.crm.deal.infrastructure.autoMappers;

import at.backend.MarketingCompany.crm.deal.infrastructure.DTOs.DealInput;
import at.backend.MarketingCompany.crm.deal.v2.infrastructure.persistence.DealEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-26T13:53:26-0600",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.11.1.jar, environment: Java 23.0.2 (Homebrew)"
)
@Component
public class DealMappersImpl implements DealMappers {

    @Override
    public DealEntity inputToEntity(DealInput input) {
        if ( input == null ) {
            return null;
        }

        DealEntity dealEntity = new DealEntity();

        dealEntity.setDealStatus( input.dealStatus() );
        dealEntity.setFinalAmount( input.finalAmount() );
        dealEntity.setStartDate( input.startDate() );
        dealEntity.setEndDate( input.endDate() );
        dealEntity.setDeliverables( input.deliverables() );
        dealEntity.setTerms( input.terms() );

        return dealEntity;
    }

    @Override
    public DealEntity inputToUpdatedEntity(DealEntity existingUser, DealInput input) {
        if ( input == null ) {
            return existingUser;
        }

        existingUser.setDealStatus( input.dealStatus() );
        existingUser.setFinalAmount( input.finalAmount() );
        existingUser.setStartDate( input.startDate() );
        existingUser.setEndDate( input.endDate() );
        existingUser.setDeliverables( input.deliverables() );
        existingUser.setTerms( input.terms() );

        return existingUser;
    }
}
