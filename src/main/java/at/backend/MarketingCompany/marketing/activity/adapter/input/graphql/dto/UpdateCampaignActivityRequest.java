package at.backend.MarketingCompany.marketing.activity.adapter.input.graphql.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpdateCampaignActivityRequest(
    @Size(max = 100, message = "Activity name cannot exceed 100 characters")
    String name,

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    String description,

    LocalDateTime actualStartDate,

    LocalDateTime actualEndDate,

    @DecimalMin(value = "0.0", message = "Actual cost cannot be negative")
    BigDecimal actualCost,

    Long assignedToUserId,

    @Size(max = 500, message = "Success criteria cannot exceed 500 characters")
    String successCriteria,

    @Size(max = 500, message = "Target audience cannot exceed 500 characters")
    String targetAudience
) {}