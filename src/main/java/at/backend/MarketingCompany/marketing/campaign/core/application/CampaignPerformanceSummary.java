package at.backend.MarketingCompany.marketing.campaign.core.application;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.CampaignStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
public record CampaignPerformanceSummary(
		Long campaignId,
		String campaignName,
		CampaignStatus status,
		BigDecimal totalBudget,
		BigDecimal spentAmount,
		BigDecimal remainingBudget,
		BigDecimal budgetUtilizationPercentage,
		BigDecimal totalAttributedRevenue,
		BigDecimal roiPercentage,
		BigDecimal netProfit,
		String roiStatus,
		Boolean isProfitable,
		Boolean needsOptimization,
		Boolean isHighPerformer
) {}
