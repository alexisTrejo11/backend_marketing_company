package at.backend.MarketingCompany.marketing.campaign.adapter.input.graphql.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Builder
public record CampaignOutput(
    Long id,
    String name,
    String description,
    String campaignType,
    String status,
    BigDecimal totalBudget,
    BigDecimal spentAmount,
    BigDecimal remainingBudget,
    BigDecimal budgetUtilizationPercentage,
    LocalDate startDate,
    LocalDate endDate,
    Boolean isActive,
    Map<String, Object> budgetAllocations,
    Map<String, Object> targetAudienceDemographics,
    Map<String, Object> targetLocations,
    Map<String, Object> targetInterests,
    String primaryGoal,
    Map<String, Object> secondaryGoals,
    Map<String, Object> successMetrics,
    Long primaryChannelId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
