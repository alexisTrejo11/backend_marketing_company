package at.backend.MarketingCompany.crm.servicePackage.core.application.command;

import java.util.List;

import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.params.CreateServicePackageParams;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.Complexity;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.EstimatedHours;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.Price;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.ProjectDuration;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.RecurrenceInfo;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.ServiceType;
import at.backend.MarketingCompany.shared.SocialNetworkPlatform;
import lombok.Builder;

@Builder
public record CreateServicePackageCommand(
    String name,
    String description,
    Price price,
    ServiceType serviceType,
    String deliverables,
    EstimatedHours estimatedHours,
    Complexity complexity,
    ProjectDuration projectDuration,
    RecurrenceInfo recurrenceInfo,
    List<String> kpis,
    List<SocialNetworkPlatform> socialNetworkPlatforms) {

  public CreateServicePackageParams toCreateParams() {
    return CreateServicePackageParams.builder()
        .name(this.name)
        .description(this.description)
        .price(this.price)
        .serviceType(this.serviceType)
        .deliverables(this.deliverables)
        .estimatedHours(this.estimatedHours())
        .complexity(this.complexity)
        .recurrenceInfo(recurrenceInfo)
        .projectDuration(this.projectDuration())
        .kpis(this.kpis)
        .socialNetworkPlatforms(this.socialNetworkPlatforms)
        .build();
  }
}
