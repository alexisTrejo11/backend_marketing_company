package at.backend.MarketingCompany.marketing.campaign.core.application;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;


@Builder
public record CampaignStatistics(
		Long totalCampaigns,
		Long activeCampaigns,
		Long plannedCampaigns,
		Long pausedCampaigns,
		Long completedCampaigns,
		Long cancelledCampaigns,
		BigDecimal totalPlannedBudget,
		BigDecimal totalActiveSpend,
		BigDecimal totalAttributedRevenue,
		BigDecimal overallROI,
		Long campaignsNeedingOptimization,
		Long highPerformingCampaigns
) {}
