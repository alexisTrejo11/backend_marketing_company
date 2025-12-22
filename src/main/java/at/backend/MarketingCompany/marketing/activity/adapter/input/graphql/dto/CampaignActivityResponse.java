package at.backend.MarketingCompany.marketing.activity.adapter.input.graphql.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public record CampaignActivityResponse(
    Long id,
    Long campaignId,
    String name,
    String description,
    String activityType,
    String status,
    LocalDateTime plannedStartDate,
    LocalDateTime plannedEndDate,
    LocalDateTime actualStartDate,
    LocalDateTime actualEndDate,
    BigDecimal plannedCost,
    BigDecimal actualCost,
    BigDecimal costOverrunPercentage,
    Boolean isDelayed,
    Boolean isCompleted,
    Long assignedToUserId,
    String deliveryChannel,
    String successCriteria,
    String targetAudience,
    Map<String, Object> dependencies,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
