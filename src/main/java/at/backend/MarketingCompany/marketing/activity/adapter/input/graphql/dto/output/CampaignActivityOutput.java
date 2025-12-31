package at.backend.MarketingCompany.marketing.activity.adapter.input.graphql.dto.output;


import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivityStatus;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivityType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Builder
public record CampaignActivityOutput(
		Long id,
		Long campaignId,
		String name,
		String description,
		ActivityType activityType,
		ActivityStatus status,
		LocalDateTime plannedStartDate,
		LocalDateTime plannedEndDate,
		LocalDateTime actualStartDate,
		LocalDateTime actualEndDate,
		BigDecimal plannedCost,
		BigDecimal actualCost,
		String targetAudience,
		Long assignedToUserId,
		String dependenciesJson,
		String createdAt,
		String updatedAt
) {
}
