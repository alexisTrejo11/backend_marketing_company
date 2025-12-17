package at.backend.MarketingCompany.crm.servicePackage.adapter.input.graphql.mapper;

import at.backend.MarketingCompany.crm.servicePackage.adapter.input.graphql.dto.ServicePackageOutput;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.ServicePackage;
import at.backend.MarketingCompany.shared.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class ServicePackageOutputMapper {

  public ServicePackageOutput toOutput(ServicePackage servicePackage) {
    if (servicePackage == null) {
      return null;
    }

    return ServicePackageOutput.builder()
        .id(servicePackage.getId() != null ? servicePackage.getId().asString() : null)
        .name(servicePackage.getName())
        .description(servicePackage.getDescription())
        .price(servicePackage.getPrice() != null ? servicePackage.getPrice().amount() : null)
        .serviceType(servicePackage.getServiceType())
        .deliverables(servicePackage.getDeliverables())
        .estimatedHours(servicePackage.getEstimatedHours() != null ? servicePackage.getEstimatedHours().hours() : null)
        .complexity(servicePackage.getComplexity())
        .isRecurring(servicePackage.getRecurrenceInfo() != null ? servicePackage.getRecurrenceInfo().isRecurring() : null)
        .frequency(servicePackage.getRecurrenceInfo() != null ? servicePackage.getRecurrenceInfo().frequency() : null)
        .projectDuration(servicePackage.getProjectDuration() != null ? servicePackage.getProjectDuration().months() : null)
        .kpis(servicePackage.getKpis())
        .socialNetworkPlatforms(servicePackage.getSocialNetworkPlatforms())
        .active(servicePackage.getActive())
        .createdAt(servicePackage.getCreatedAt())
        .updatedAt(servicePackage.getUpdatedAt())
        .build();
  }

  public PageResponse<ServicePackageOutput> toPageOutput(Page<ServicePackage> servicePackagePage) {
    if (servicePackagePage == null) {
      return null;
    }

    return PageResponse.of(servicePackagePage.map(this::toOutput));
  }
}
