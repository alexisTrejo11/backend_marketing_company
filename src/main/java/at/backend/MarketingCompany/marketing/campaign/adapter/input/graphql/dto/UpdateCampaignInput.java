package at.backend.MarketingCompany.marketing.campaign.adapter.input.graphql.dto;

import at.backend.MarketingCompany.marketing.ab_test.core.domain.valueobject.CampaignType;
import at.backend.MarketingCompany.marketing.campaign.core.application.command.UpdateCampaignCommand;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.*;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.MarketingChannelId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.Map;

public record UpdateCampaignInput(
    @NotNull @Positive Long id,
    String name,
    String description,
    CampaignType campaignType,
		LocalDate startDate,
    LocalDate endDate,
    String primaryGoal,
    Long primaryChannelId,
    Map<String, Object> targetAudienceDemographics,
    Map<String, Object> targetLocations,
    String targetInterests
) {
	public UpdateCampaignCommand toCommand() {
		CampaignPeriod period = null;
		if (endDate != null && startDate != null) {
			period = new CampaignPeriod(startDate, endDate);
		}

		return UpdateCampaignCommand.builder()
				.campaignId(new MarketingCampaignId(this.id))
				.name(this.name != null ? new CampaignName(this.name) : null)
				.period(period)
				.description(this.description)
				.campaignType(this.campaignType)
				.primaryGoal(this.primaryGoal)
				.primaryChannelId(this.primaryChannelId != null ? new MarketingChannelId(this.primaryChannelId) : null)
				.targetAudienceDemographics(this.targetAudienceDemographics)
				.targetLocations(this.targetLocations)
				.build();
	}
}