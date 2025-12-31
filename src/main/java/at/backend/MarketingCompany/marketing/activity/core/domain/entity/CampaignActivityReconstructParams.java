package at.backend.MarketingCompany.marketing.activity.core.domain.entity;

import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.*;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
public record CampaignActivityReconstructParams(
    CampaignActivityId id,
    MarketingCampaignId campaignId,
    String name,
    String description,
    ActivityType activityType,
    ActivityStatus status,
    ActivitySchedule schedule,
    ActivityCost cost,
    Long assignedToUserId,
    String deliveryChannel,
    String successCriteria,
    String targetAudience,
    Object dependencies,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime deletedAt,
    Integer version,
    // TODO: Add audit fields if necessary
    LocalDateTime statusChangedAt,
    String lastStatusChangeReason,
    Long lastModifiedByUserId
) {}