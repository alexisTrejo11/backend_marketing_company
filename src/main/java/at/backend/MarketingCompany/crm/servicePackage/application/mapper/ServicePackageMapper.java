package at.backend.MarketingCompany.crm.servicePackage.application.mapper;

import at.backend.MarketingCompany.crm.servicePackage.application.dto.ServicePackageResponse;
import at.backend.MarketingCompany.crm.servicePackage.domain.entity.ServicePackage;
import org.springframework.stereotype.Component;

@Component
public class ServicePackageMapper {

    public ServicePackageResponse toResponse(ServicePackage servicePackage) {
        return new ServicePackageResponse(
                servicePackage.getId().value(),
                servicePackage.getName(),
                servicePackage.getDescription(),
                servicePackage.getPrice().amount(),
                servicePackage.getServiceType(),
                servicePackage.getDeliverables(),
                servicePackage.getEstimatedHours().hours(),
                servicePackage.getComplexity(),
                servicePackage.getRecurrenceInfo().isRecurring(),
                servicePackage.getRecurrenceInfo().frequency(),
                servicePackage.getProjectDuration() != null ? servicePackage.getProjectDuration().months() : null,
                servicePackage.getKpis(),
                servicePackage.getSocialNetworkPlatforms(),
                servicePackage.getActive(),
                servicePackage.getCreatedAt(),
                servicePackage.getUpdatedAt()
        );
    }
}