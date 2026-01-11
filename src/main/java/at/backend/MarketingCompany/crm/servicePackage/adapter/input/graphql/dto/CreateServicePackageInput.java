package at.backend.MarketingCompany.crm.servicePackage.adapter.input.graphql.dto;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import at.backend.MarketingCompany.crm.servicePackage.core.application.command.CreateServicePackageCommand;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.Complexity;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.EstimatedHours;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.Frequency;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.Price;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.ProjectDuration;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.RecurrenceInfo;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.ServiceType;
import at.backend.MarketingCompany.shared.SocialNetworkPlatform;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateServicePackageInput(
    @NotBlank(message = "Name is required") @Length(max = 100, min = 3) String name,

    @Length(max = 500, message = "Description cannot exceed 500 characters") String description,

    @NotNull(message = "Price is required") @DecimalMax(value = "1000000.00", message = "Price must be less than or equal to 1000000.00") BigDecimal price,

    @NotNull(message = "Service type is required") ServiceType serviceType,

    @Length(max = 1000, message = "Deliverables cannot exceed 1000 characters") String deliverables,

    @NotNull(message = "Estimated hours is required") @Min(value = 1, message = "Estimated hours must be at least 1") Integer estimatedHours,

    @NotNull(message = "Complexity is required") Complexity complexity,

    @NotNull(message = "IsRecurring is required") Boolean isRecurring,

    Frequency frequency,

    Integer projectDuration,

    List<String> kpis,

    List<SocialNetworkPlatform> socialNetworkPlatforms) {
  public CreateServicePackageCommand toCommand() {
    var builder = CreateServicePackageCommand.builder()
        .name(name)
        .description(description)
        .price(price != null ? Price.of(price) : null)
        .serviceType(serviceType)
        .deliverables(deliverables)
        .estimatedHours(estimatedHours != null ? EstimatedHours.create(estimatedHours) : null)
        .complexity(complexity)
        .projectDuration(projectDuration != null ? ProjectDuration.create(projectDuration) : null)
        .kpis(kpis != null ? kpis : List.of())
        .socialNetworkPlatforms(socialNetworkPlatforms != null ? socialNetworkPlatforms : List.of());

    if (isRecurring != null && frequency != null) {
      RecurrenceInfo recurrenceInfo = RecurrenceInfo.create(isRecurring, frequency);
      builder.recurrenceInfo(recurrenceInfo);
    }

    return builder.build();
  }
}
