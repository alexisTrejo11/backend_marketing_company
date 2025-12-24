package at.backend.MarketingCompany.marketing.activity.core.application.command;

import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivityType;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public record CreateActivityCommand(
    MarketingCampaignId campaignId,
    String name,
    String description,
    ActivityType activityType,
    LocalDateTime plannedStartDate,
    LocalDateTime plannedEndDate,
    BigDecimal plannedCost,
    Long assignedToUserId,
    String deliveryChannel,
    String successCriteria,
    String targetAudience,
    Map<String, Object> dependencies
) {}