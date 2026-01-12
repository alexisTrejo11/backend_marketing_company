package at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.params;

import java.math.BigDecimal;
import java.util.List;

import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.Complexity;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.EstimatedHours;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.Price;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.ProjectDuration;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.RecurrenceInfo;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.ServiceType;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.exceptions.ServicePackageValidationException;
import at.backend.MarketingCompany.shared.SocialNetworkPlatform;
import lombok.Builder;

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
    List<SocialNetworkPlatform> socialNetworkPlatforms) {

  public CreateServicePackageParams {
    if (name == null || name.isBlank()) {
      throw new ServicePackageValidationException("Service package name cannot be null or empty");
    }
    if (price == null || price.amount().compareTo(BigDecimal.ZERO) < 0) {
      throw new ServicePackageValidationException("Service package price cannot be null or negative");
    }

    if (estimatedHours == null) {
      throw new ServicePackageValidationException("Service package estimated hours cannot be null");
    }

    if (serviceType == null) {
      throw new ServicePackageValidationException("Service package type cannot be null");
    }

    if (complexity == null) {
      throw new ServicePackageValidationException("Service package complexity cannot be null");
    }

    if (recurrenceInfo == null) {
      throw new ServicePackageValidationException("Service package recurrence info cannot be null");
    }
    if (kpis == null) {
      kpis = List.of();
    }

    if (socialNetworkPlatforms == null) {
      socialNetworkPlatforms = List.of();
    }
  }
}
