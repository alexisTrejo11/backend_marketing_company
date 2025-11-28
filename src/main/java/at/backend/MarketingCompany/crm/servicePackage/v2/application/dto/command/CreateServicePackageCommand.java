package at.backend.MarketingCompany.crm.servicePackage.v2.application.dto.command;

import at.backend.MarketingCompany.crm.Utils.enums.*;
import at.backend.MarketingCompany.crm.servicePackage.v2.domain.entity.valueobjects.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public record CreateServicePackageCommand(
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

    public CreateServicePackageParams toCreateParams() {
        return CreateServicePackageParams.builder()
                .name(this.name)
                .description(this.description)
                .price(Price.of(this.price))
                .serviceType(this.serviceType)
                .deliverables(this.deliverables)
                .estimatedHours(EstimatedHours.of(this.estimatedHours()))
                .complexity(this.complexity)
                .recurrenceInfo(RecurrenceInfo.create(this.isRecurring(), this.frequency()))
                .projectDuration(ProjectDuration.of(this.projectDuration()))
                .kpis(this.kpis)
                .socialNetworkPlatforms(this.socialNetworkPlatforms)
                .build();
        }
}