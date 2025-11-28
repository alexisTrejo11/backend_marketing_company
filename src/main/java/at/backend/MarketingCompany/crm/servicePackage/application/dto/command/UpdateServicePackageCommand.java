package at.backend.MarketingCompany.crm.servicePackage.application.dto.command;

import at.backend.MarketingCompany.crm.servicePackage.domain.entity.valueobjects.*;
import at.backend.MarketingCompany.crm.shared.enums.Complexity;
import at.backend.MarketingCompany.crm.shared.enums.ServiceType;
import at.backend.MarketingCompany.crm.shared.enums.SocialNetworkPlatform;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public record UpdateServicePackageCommand(
    ServicePackageId id,


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
    public UpdateServicePackageParams toUpdateParams() {
        return new UpdateServicePackageParams(
                this.name(),
                this.description(),
                Price.of(this.price()),
                this.serviceType(),
                this.deliverables(),
                EstimatedHours.of(this.estimatedHours()),
                this.complexity(),
                RecurrenceInfo.create(this.isRecurring(), this.frequency()),
                ProjectDuration.of(this.projectDuration()),
                this.kpis(),
                this.socialNetworkPlatforms()
        );
    }
}