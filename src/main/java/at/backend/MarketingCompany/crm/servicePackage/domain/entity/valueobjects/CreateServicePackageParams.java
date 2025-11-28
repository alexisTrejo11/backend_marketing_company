package at.backend.MarketingCompany.crm.servicePackage.domain.entity.valueobjects;

import at.backend.MarketingCompany.common.exceptions.MissingFieldException;
import at.backend.MarketingCompany.crm.shared.enums.Complexity;
import at.backend.MarketingCompany.crm.shared.enums.ServiceType;
import at.backend.MarketingCompany.crm.shared.enums.SocialNetworkPlatform;
import lombok.Builder;

import java.util.List;

@Builder
public record CreateServicePackageParams(
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

    public CreateServicePackageParams {
        if (name == null) {
            throw new MissingFieldException("CreateServicePackageParams", "name");
        }
        if (description == null) {
            throw new MissingFieldException("CreateServicePackageParams", "description");
        }
        if (serviceType == null) {
            throw new MissingFieldException("CreateServicePackageParams", "serviceType");
        }


        if (kpis == null) {
            kpis = List.of();
        }

        if (socialNetworkPlatforms == null) {
            socialNetworkPlatforms = List.of();
        }
    }
}
