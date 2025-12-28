package at.backend.MarketingCompany.marketing.activity.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.marketing.activity.core.application.command.CreateActivityCommand;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivityType;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public record CreateCampaignActivityInput(
		@NotNull(message = "Campaign ID is required")
		@Positive(message = "Campaign ID must be positive")
		Long campaignId,

		@NotBlank(message = "Activity name is required")
		@Size(max = 100, message = "Activity name cannot exceed 100 characters")
		String name,

		@Size(max = 1000, message = "Description cannot exceed 1000 characters")
		String description,

		@NotNull(message = "Activity type is required")
		ActivityType activityType,

		@NotNull(message = "Planned start date is required")
		@Future(message = "Planned start date must be in the future")
		LocalDateTime plannedStartDate,

		@NotNull(message = "Planned end date is required")
		LocalDateTime plannedEndDate,

		@NotNull(message = "Planned cost is required")
		@DecimalMin(value = "0.01", message = "Planned cost must be greater than 0")
		BigDecimal plannedCost,

		@Positive(message = "Assigned user ID must be positive")
		Long assignedToUserId,

		@NotBlank(message = "Delivery channel is required")
		@Size(max = 50, message = "Delivery channel cannot exceed 50 characters")
		String deliveryChannel,

		@Size(max = 500, message = "Success criteria cannot exceed 500 characters")
		String successCriteria,

		@Size(max = 500, message = "Target audience cannot exceed 500 characters")
		String targetAudience,

		Map<String, Object> dependencies
) {

	public CreateActivityCommand toCommand() {
		return CreateActivityCommand.builder()
				.campaignId(campaignId != null ? new MarketingCampaignId(campaignId) : null)
				.name(name)
				.description(description)
				.activityType(activityType)
				.plannedStartDate(plannedStartDate)
				.plannedEndDate(plannedEndDate)
				.plannedCost(plannedCost)
				.assignedToUserId(assignedToUserId)
				.deliveryChannel(deliveryChannel)
				.successCriteria(successCriteria)
				.targetAudience(targetAudience)
				.dependencies(dependencies)
				.build();
	}
}