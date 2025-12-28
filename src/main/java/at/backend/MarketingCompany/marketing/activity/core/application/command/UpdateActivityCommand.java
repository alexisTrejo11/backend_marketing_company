package at.backend.MarketingCompany.marketing.activity.core.application.command;

import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivityType;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.CampaignActivityId;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
public record UpdateActivityCommand(
		CampaignActivityId activityId,
		String name,
		String description,
		ActivityType activityType,
		LocalDateTime plannedStartDate,
		LocalDateTime plannedEndDate,
		Long assignedToUserId,
		String successCriteria,
		String targetAudience,
		Map<String, Object> dependencies
) {}