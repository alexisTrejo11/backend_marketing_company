package at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects;

import at.backend.MarketingCompany.shared.domain.exceptions.MissingFieldException;
import at.backend.MarketingCompany.shared.SocialNetworkPlatform;
import lombok.Builder;

import java.util.List;

@Builder
public record UpdateServicePackageParams(
        String name,
        String description,
        Price price,
        ServiceType serviceType,
        String deliverables,
        EstimatedHours estimatedHours,
        Complexity complexity,
        RecurrenceInfo recurrenceInfo,
        ProjectDuration projectDuration,
        List<String> kpis,
        List<SocialNetworkPlatform> socialNetworkPlatforms
) {

    public UpdateServicePackageParams {
        if (name == null) {
            throw new MissingFieldException("CreateServicePackageParams", "name");
        }
        if (description == null) {
            throw new MissingFieldException("CreateServicePackageParams", "description");
        }
        if (serviceType == null) {
            throw new MissingFieldException("CreateServicePackageParams", "serviceType");
        }
    }
}
