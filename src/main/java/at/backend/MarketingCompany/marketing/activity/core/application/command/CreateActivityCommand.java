package at.backend.MarketingCompany.marketing.activity.core.application.command;

import at.backend.MarketingCompany.marketing.activity.core.domain.entity.CreateActivityParams;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivityCost;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivitySchedule;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivityType;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Builder
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
) {

	public CreateActivityParams toCreateActivityParams() {
		var schedule = ActivitySchedule.createPlannedSchedule(plannedStartDate,  plannedEndDate);
		var cost = ActivityCost.create(plannedCost);

		return CreateActivityParams.builder()
				.campaignId(campaignId)
				.name(name)
				.activityType(activityType)
				.schedule(schedule)
				.cost(cost)
				.deliveryChannel(deliveryChannel)
				.dependencies(dependencies)
				.description(description)
				.successCriteria(successCriteria)
				.targetAudience(targetAudience)
				.build();
	}
}