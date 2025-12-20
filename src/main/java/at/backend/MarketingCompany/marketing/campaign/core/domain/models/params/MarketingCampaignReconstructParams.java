package at.backend.MarketingCompany.marketing.campaign.core.domain.models.params;

import at.backend.MarketingCompany.marketing.campaign.core.domain.models.CampaignTarget;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.*;


import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public record MarketingCampaignReconstructParams(
    MarketingCampaignId id,
    CampaignName name,
    String description,
    CampaignType campaignType,
    CampaignStatus status,
    CampaignBudget budget,
    CampaignPeriod period,
    Map<String, Object> budgetAllocations,
    Map<String, Object> targetAudienceDemographics,
    Map<String, Object> targetLocations,
    Map<String, Object> targetInterests,
    String primaryGoal,
    Map<String, Object> secondaryGoals,
    Map<String, Object> successMetrics,
    MarketingChannelId primaryChannelId,
    Set<CampaignTarget> targets,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime deletedAt,
    Integer version
) {}