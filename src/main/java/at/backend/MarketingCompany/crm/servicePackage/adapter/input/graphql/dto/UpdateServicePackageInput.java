package at.backend.MarketingCompany.crm.servicePackage.adapter.input.graphql.dto;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import at.backend.MarketingCompany.crm.servicePackage.core.application.command.UpdateServicePackageCommand;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.Complexity;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.EstimatedHours;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.Frequency;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.Price;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.ProjectDuration;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.RecurrenceInfo;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.ServicePackageId;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.ServiceType;
import at.backend.MarketingCompany.shared.SocialNetworkPlatform;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UpdateServicePackageInput(
    @NotNull @Positive Long id,

    @NotBlank @Size(max = 100, message = "Name cannot exceed 100 characters") String name,

    String description,

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero") BigDecimal price,

    ServiceType serviceType,

    @Length(max = 500, message = "Deliverables cannot exceed 500 characters") String deliverables,

    @Min(value = 1, message = "Estimated hours must be at least 1") Integer estimatedHours,

    Complexity complexity,

    Boolean isRecurring,

    Frequency frequency,

    Integer projectDuration,

    List<String> kpis,

    List<SocialNetworkPlatform> socialNetworkPlatforms) {
  public UpdateServicePackageCommand toCommand() {
    RecurrenceInfo recurrenceInfo = null;
    if (isRecurring != null && frequency != null) {
      recurrenceInfo = RecurrenceInfo.create(isRecurring, frequency);
    }

    return UpdateServicePackageCommand.builder()
        .id(new ServicePackageId(this.id))
        .name(this.name)
        .description(this.description)
        .price(this.price != null ? new Price(this.price) : null)
        .serviceType(this.serviceType)
        .deliverables(this.deliverables)
        .estimatedHours(this.estimatedHours != null ? EstimatedHours.create(this.estimatedHours) : null)
        .complexity(this.complexity)
        .recurrenceInfo(recurrenceInfo)
        .projectDuration(this.projectDuration != null ? ProjectDuration.create(this.projectDuration) : null)
        .kpis(this.kpis != null ? this.kpis : List.of())
        .socialNetworkPlatforms(this.socialNetworkPlatforms != null ? this.socialNetworkPlatforms : List.of())
        .build();
  }
}
