package at.backend.MarketingCompany.marketing.campaign.adapter.input.graphql.dto;

import at.backend.MarketingCompany.marketing.campaign.core.application.command.CreateCampaignCommand;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.CampaignName;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.CampaignPeriod;
import at.backend.MarketingCompany.marketing.ab_test.core.domain.valueobject.CampaignType;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.MarketingChannelId;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public record CreateCampaignInput(
    @NotBlank(message = "Campaign name is required")
    @Size(max = 100, message = "Campaign name cannot exceed 100 characters")
    String name,

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    String description,

    @NotNull(message = "Campaign type is required")
    CampaignType campaignType,

    @NotNull(message = "Total budget is required")
    @DecimalMin(value = "0.01", message = "Total budget must be greater than 0")
    BigDecimal totalBudget,

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be in the present or future")
    LocalDate startDate,

    LocalDate endDate,

    Map<String, Object> budgetAllocations,

    Map<String, Object> targetAudienceDemographics,

    Map<String, Object> targetLocations,

    Map<String, Object> targetInterests,

    @Size(max = 200, message = "Primary goal cannot exceed 200 characters")
    String primaryGoal,

    Map<String, Object> secondaryGoals,

    Map<String, Object> successMetrics,

    @Positive(message = "Channel ID must be positive")
    Long primaryChannelId
) {

	public CreateCampaignCommand toCommand() {
		return CreateCampaignCommand.builder()
				.name(new CampaignName(this.name))
				.description(this.description)
				.campaignType(this.campaignType)
				.totalBudget(this.totalBudget)
				.period(new CampaignPeriod(this.startDate, this.endDate))
				.primaryGoal(this.primaryGoal)
				.targetAudienceDemographics(this.targetAudienceDemographics)
				.targetLocations(this.targetLocations)
				.primaryChannelId(this.primaryChannelId != null ? new MarketingChannelId(this.primaryChannelId) : null)
				.build();
	}
}