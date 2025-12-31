package at.backend.MarketingCompany.marketing.activity.core.application.command;

import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivityType;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.CampaignActivityId;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
public record UpdateActivityCommand(
		CampaignActivityId activityId,
		String name,
		String description,
		String successCriteria,
		String targetAudience,
		JsonNode dependencies
) {}