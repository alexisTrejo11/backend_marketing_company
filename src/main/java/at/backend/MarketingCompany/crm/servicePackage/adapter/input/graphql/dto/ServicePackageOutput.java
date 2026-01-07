package at.backend.MarketingCompany.crm.servicePackage.adapter.input.graphql.dto;

import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.Complexity;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.Frequency;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.ServiceType;
import at.backend.MarketingCompany.shared.SocialNetworkPlatform;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ServicePackageOutput(
    String id,
    String name,
    String description,
    BigDecimal price,
    ServiceType serviceType,
    String deliverables,
    Integer estimatedHours,
    Complexity complexity,
    Boolean isRecurring,
    Frequency frequency,
    Integer projectDuration,
    List<String> kpis,
    List<SocialNetworkPlatform> socialNetworkPlatforms,
    Boolean active,
    String createdAt,
    String updatedAt) {

}
