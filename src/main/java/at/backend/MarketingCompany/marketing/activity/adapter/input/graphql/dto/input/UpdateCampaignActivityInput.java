package at.backend.MarketingCompany.marketing.activity.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.marketing.activity.core.application.command.UpdateActivityCommand;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivityType;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.CampaignActivityId;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public record UpdateCampaignActivityInput(
		@NotNull(message = "ID is required")
		@Positive(message = "ID must be positive")
		Long id,

		@Size(max = 100, message = "Activity name cannot exceed 100 characters")
		String name,

		@Size(max = 1000, message = "Description cannot exceed 1000 characters")
		String description,

		@DecimalMin(value = "0.0", message = "Actual cost cannot be negative")
		BigDecimal actualCost,

		@Size(max = 500, message = "Success criteria cannot exceed 500 characters")
		String successCriteria,

		@Size(max = 500, message = "Target audience cannot exceed 500 characters")
		String targetAudience,

		JsonNode dependencies

) {

	public UpdateActivityCommand toCommand() {
		return UpdateActivityCommand.builder()
				.activityId(new CampaignActivityId(id))
				.name(name)
				.description(description)
				.successCriteria(successCriteria)
				.targetAudience(targetAudience)
				.dependencies(dependencies)
				.build();
	}
}