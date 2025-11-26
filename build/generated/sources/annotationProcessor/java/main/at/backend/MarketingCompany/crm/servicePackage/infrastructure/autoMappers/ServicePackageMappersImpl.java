package at.backend.MarketingCompany.crm.servicePackage.infrastructure.autoMappers;

import at.backend.MarketingCompany.crm.Utils.enums.SocialNetworkPlatform;
import at.backend.MarketingCompany.crm.servicePackage.domain.ServicePackage;
import at.backend.MarketingCompany.crm.servicePackage.infrastructure.DTOs.ServicePackageInput;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-26T13:53:26-0600",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.11.1.jar, environment: Java 23.0.2 (Homebrew)"
)
@Component
public class ServicePackageMappersImpl implements ServicePackageMappers {

    @Override
    public ServicePackage inputToEntity(ServicePackageInput input) {
        if ( input == null ) {
            return null;
        }

        ServicePackage servicePackage = new ServicePackage();

        servicePackage.setName( input.name() );
        servicePackage.setDescription( input.description() );
        servicePackage.setPrice( input.price() );
        servicePackage.setServiceType( input.serviceType() );
        servicePackage.setDeliverables( input.deliverables() );
        servicePackage.setEstimatedHours( input.estimatedHours() );
        servicePackage.setComplexity( input.complexity() );
        servicePackage.setIsRecurring( input.isRecurring() );
        servicePackage.setFrequency( input.frequency() );
        servicePackage.setProjectDuration( input.projectDuration() );
        List<String> list = input.kpis();
        if ( list != null ) {
            servicePackage.setKpis( new ArrayList<String>( list ) );
        }
        List<SocialNetworkPlatform> list1 = input.socialNetworkPlatforms();
        if ( list1 != null ) {
            servicePackage.setSocialNetworkPlatforms( new ArrayList<SocialNetworkPlatform>( list1 ) );
        }
        servicePackage.setActive( input.active() );

        return servicePackage;
    }

    @Override
    public ServicePackage inputToUpdatedEntity(ServicePackage existingUser, ServicePackageInput input) {
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
