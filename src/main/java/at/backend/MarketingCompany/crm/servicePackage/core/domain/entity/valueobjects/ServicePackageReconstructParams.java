package at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects;

import at.backend.MarketingCompany.shared.SocialNetworkPlatform;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ServicePackageReconstructParams(
        ServicePackageId id,
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
        List<SocialNetworkPlatform> socialNetworkPlatforms,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt,
        Integer version
) {
}
