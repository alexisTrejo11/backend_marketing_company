package at.backend.MarketingCompany.crm.servicePackage.v2.application.dto;

import at.backend.MarketingCompany.crm.Utils.enums.*;
import at.backend.MarketingCompany.crm.servicePackage.v2.domain.entity.valueobjects.Frequency;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ServicePackageResponse(
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
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}