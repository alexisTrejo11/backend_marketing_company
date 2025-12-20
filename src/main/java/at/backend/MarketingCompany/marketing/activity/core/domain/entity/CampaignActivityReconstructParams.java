package at.backend.MarketingCompany.marketing.activity.core.domain.entity;

import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.CampaignActivityId;
import at.backend.MarketingCompany.marketing.core.domain.valueobject.*;
import at.backend.MarketingCompany.marketing.core.domain.entity.CampaignActivity.ActivityType;
import at.backend.MarketingCompany.marketing.core.domain.entity.CampaignActivity.ActivityStatus;
import java.time.LocalDateTime;
import java.util.Map;

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
    Map<String, Object> dependencies,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime deletedAt,
    Integer version
) {}