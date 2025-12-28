package at.backend.MarketingCompany.marketing.activity.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.marketing.activity.core.application.query.ActivityQuery;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivityStatus;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivityType;

import java.time.LocalDateTime;
import java.util.List;

public record SearchActivityInput(
		Long campaignId,
		List<ActivityStatus> statuses,
		List<ActivityType> activityTypes,
		Long assignedToUserId,
		LocalDateTime plannedStartFrom,
		LocalDateTime plannedStartTo,
		Boolean isDelayed,
		Boolean isCompleted,
		String searchTerm
) {

	public ActivityQuery toQuery() {
		return new ActivityQuery(
				campaignId,
				statuses,
				activityTypes,
				assignedToUserId,
				plannedStartFrom,
				plannedStartTo,
				isDelayed,
				isCompleted,
				searchTerm
		);
	}
}
