package at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.params;

import java.time.LocalDateTime;
import java.util.List;

import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.Complexity;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.EstimatedHours;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.Price;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.ProjectDuration;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.RecurrenceInfo;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.ServicePackageId;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.ServiceType;
import at.backend.MarketingCompany.shared.SocialNetworkPlatform;
import lombok.Builder;

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
    Integer version) {
}
