package at.backend.MarketingCompany.marketing.campaign.adapter.input.graphql.dto;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.CampaignROI;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.CampaignStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Builder
public record CampaignOutput(
		Long id,
		String name,
		String description,
		String campaignType,
		CampaignStatus status,
		CampaignBudget budget,
		CampaignPeriod period,
		String primaryGoal,
		Map<String, Object> budgetAllocations,
		Map<String, Object> targetAudienceDemographics,
		Map<String, Object> targetLocations,
		Map<String, Object> targetInterests,
		Map<String, Object> secondaryGoals,
		Map<String, Object> successMetrics,
		CampaignROI roi,
		boolean needsOptimization,
		Long primaryChannelId,
		LocalDateTime createdAt,
		LocalDateTime updatedAt
) {
	@Builder
	public record CampaignBudget(
			BigDecimal totalBudget,
			BigDecimal spentAmount,
			BigDecimal remainingBudget,
			BigDecimal utilizationPercentage,
			Boolean isNearlyExhausted
	) {
	}

	@Builder
	public record CampaignPeriod(
			LocalDate startDate,
			LocalDate endDate,
			Boolean isActive,
			Boolean hasStarted,
			Boolean hasEnded,
			Long durationInDays
	) {
	}

	@Builder
	public record CampaignROI(
			BigDecimal totalRevenue,
			BigDecimal totalCost,
			BigDecimal roiPercentage,
			BigDecimal netProfit,
			at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.CampaignROI.ROIStatus status,
			Boolean isProfitable
	) {
	}

}
