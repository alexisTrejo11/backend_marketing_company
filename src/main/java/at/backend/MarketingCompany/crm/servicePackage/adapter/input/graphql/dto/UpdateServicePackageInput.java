package at.backend.MarketingCompany.crm.servicePackage.adapter.input.graphql.dto;

import at.backend.MarketingCompany.crm.servicePackage.core.application.dto.command.UpdateServicePackageCommand;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.*;
import at.backend.MarketingCompany.shared.SocialNetworkPlatform;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public record UpdateServicePackageInput(
		@NotNull @Positive
		Long id,

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    String name,

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
  public UpdateServicePackageCommand toCommand() {
    return UpdateServicePackageCommand.builder()
        .id(new ServicePackageId(this.id))
        .name(this.name)
        .description(this.description)
        .price(this.price != null ? Price.of(this.price) : null)
        .serviceType(this.serviceType)
        .deliverables(this.deliverables)
        .estimatedHours(this.estimatedHours != null ? new EstimatedHours(this.estimatedHours) : null)
        .complexity(this.complexity)
        .recurrenceInfo((this.isRecurring != null && this.frequency != null) ? new RecurrenceInfo(this.isRecurring, this.frequency) : null)
        .projectDuration(this.projectDuration != null ? new ProjectDuration(this.projectDuration) : null)
        .kpis(this.kpis != null ? this.kpis : List.of())
        .socialNetworkPlatforms(this.socialNetworkPlatforms != null ? this.socialNetworkPlatforms : List.of())
        .build();
  }
}