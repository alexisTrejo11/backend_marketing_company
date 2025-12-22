package at.backend.MarketingCompany.marketing.campaign.core.application.command;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.CampaignName;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.CampaignPeriod;
import at.backend.MarketingCompany.marketing.ab_test.core.domain.valueobject.CampaignType;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.MarketingChannelId;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Map;

@Builder
public record CreateCampaignCommand(
		CampaignName name,
		String description,
		CampaignType campaignType,
		BigDecimal totalBudget,
		CampaignPeriod period,
		String primaryGoal,
		Map<String, Object> targetAudienceDemographics,
		Map<String, Object> targetLocations,
		MarketingChannelId primaryChannelId
) {}
