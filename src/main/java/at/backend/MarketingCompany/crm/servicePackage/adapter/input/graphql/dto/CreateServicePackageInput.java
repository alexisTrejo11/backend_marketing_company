package at.backend.MarketingCompany.crm.servicePackage.adapter.input.graphql.dto;

import at.backend.MarketingCompany.crm.servicePackage.core.application.dto.command.CreateServicePackageCommand;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.*;
import at.backend.MarketingCompany.shared.SocialNetworkPlatform;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public record CreateServicePackageInput(
    @NotBlank(message = "Name is required")
    @Size(max = 100, min = 3)
    String name,

    @Size(max = 500)
    String description,

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    BigDecimal price,

    @NotNull(message = "Service type is required")
    ServiceType serviceType,

    String deliverables,

    @NotNull(message = "Estimated hours is required")
    @Min(value = 1, message = "Estimated hours must be at least 1")
    Integer estimatedHours,

    @NotNull(message = "Complexity is required")
    Complexity complexity,

    @NotNull(message = "IsRecurring is required")
    Boolean isRecurring,

    Frequency frequency,

    Integer projectDuration,

    List<String> kpis,

    List<SocialNetworkPlatform> socialNetworkPlatforms
) {
  public CreateServicePackageCommand toCommand() {
    var builder = CreateServicePackageCommand.builder()
        .name(name)
        .description(description)
        .price(price != null ? Price.of(price) : null)
        .serviceType(serviceType)
        .deliverables(deliverables)
        .estimatedHours(estimatedHours != null ? new EstimatedHours(estimatedHours) : null)
        .complexity(complexity)
        .projectDuration(projectDuration != null ? new ProjectDuration(projectDuration) : null)
        .kpis(kpis != null ? kpis : List.of())
        .socialNetworkPlatforms(socialNetworkPlatforms != null ? socialNetworkPlatforms : List.of());

    if (isRecurring != null && frequency != null) {
      RecurrenceInfo recurrenceInfo = new RecurrenceInfo(isRecurring, frequency);
      builder.recurrenceInfo(recurrenceInfo);
    }

    return builder.build();
  }
}
