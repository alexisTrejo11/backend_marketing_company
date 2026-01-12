package at.backend.MarketingCompany.marketing.activity.core.domain.entity;

import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivityCost;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivitySchedule;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivityType;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;

import java.util.Map;

@Builder
public record CreateActivityParams(
		MarketingCampaignId campaignId,
		String name,
		ActivityType activityType,
		ActivitySchedule schedule,
		ActivityCost cost,
		String deliveryChannel,
		JsonNode dependencies,
		String description,
		String successCriteria,
		String targetAudience
) {
}
