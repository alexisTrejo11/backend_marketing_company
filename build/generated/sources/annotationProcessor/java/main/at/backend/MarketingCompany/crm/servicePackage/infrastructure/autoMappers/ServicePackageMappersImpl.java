package at.backend.MarketingCompany.crm.servicePackage.infrastructure.autoMappers;

import at.backend.MarketingCompany.crm.Utils.enums.SocialNetworkPlatform;
import at.backend.MarketingCompany.crm.servicePackage.domain.ServicePackageEntity;
import at.backend.MarketingCompany.crm.servicePackage.infrastructure.DTOs.ServicePackageInput;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-27T00:26:56-0600",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.11.1.jar, environment: Java 23.0.2 (Homebrew)"
)
@Component
public class ServicePackageMappersImpl implements ServicePackageMappers {

    @Override
    public ServicePackageEntity inputToEntity(ServicePackageInput input) {
        if ( input == null ) {
            return null;
        }

        ServicePackageEntity servicePackageEntity = new ServicePackageEntity();

        servicePackageEntity.setName( input.name() );
        servicePackageEntity.setDescription( input.description() );
        servicePackageEntity.setPrice( input.price() );
        servicePackageEntity.setServiceType( input.serviceType() );
        servicePackageEntity.setDeliverables( input.deliverables() );
        servicePackageEntity.setEstimatedHours( input.estimatedHours() );
        servicePackageEntity.setComplexity( input.complexity() );
        servicePackageEntity.setIsRecurring( input.isRecurring() );
        servicePackageEntity.setFrequency( input.frequency() );
        servicePackageEntity.setProjectDuration( input.projectDuration() );
        List<String> list = input.kpis();
        if ( list != null ) {
            servicePackageEntity.setKpis( new ArrayList<String>( list ) );
        }
        List<SocialNetworkPlatform> list1 = input.socialNetworkPlatforms();
        if ( list1 != null ) {
            servicePackageEntity.setSocialNetworkPlatforms( new ArrayList<SocialNetworkPlatform>( list1 ) );
        }
        servicePackageEntity.setActive( input.active() );

        return servicePackageEntity;
    }

    @Override
    public ServicePackageEntity inputToUpdatedEntity(ServicePackageEntity existingUser, ServicePackageInput input) {
        if ( input == null ) {
            return existingUser;
        }

        existingUser.setName( input.name() );
        existingUser.setDescription( input.description() );
        existingUser.setPrice( input.price() );
        existingUser.setServiceType( input.serviceType() );
        existingUser.setDeliverables( input.deliverables() );
        existingUser.setEstimatedHours( input.estimatedHours() );
        existingUser.setComplexity( input.complexity() );
        existingUser.setIsRecurring( input.isRecurring() );
        existingUser.setFrequency( input.frequency() );
        existingUser.setProjectDuration( input.projectDuration() );
        if ( existingUser.getKpis() != null ) {
            List<String> list = input.kpis();
            if ( list != null ) {
                existingUser.getKpis().clear();
                existingUser.getKpis().addAll( list );
            }
            else {
                existingUser.setKpis( null );
            }
        }
        else {
            List<String> list = input.kpis();
            if ( list != null ) {
                existingUser.setKpis( new ArrayList<String>( list ) );
            }
        }
        if ( existingUser.getSocialNetworkPlatforms() != null ) {
            List<SocialNetworkPlatform> list1 = input.socialNetworkPlatforms();
            if ( list1 != null ) {
                existingUser.getSocialNetworkPlatforms().clear();
                existingUser.getSocialNetworkPlatforms().addAll( list1 );
            }
            else {
                existingUser.setSocialNetworkPlatforms( null );
            }
        }
        else {
            List<SocialNetworkPlatform> list1 = input.socialNetworkPlatforms();
            if ( list1 != null ) {
                existingUser.setSocialNetworkPlatforms( new ArrayList<SocialNetworkPlatform>( list1 ) );
            }
        }
        existingUser.setActive( input.active() );

        return existingUser;
    }
}
