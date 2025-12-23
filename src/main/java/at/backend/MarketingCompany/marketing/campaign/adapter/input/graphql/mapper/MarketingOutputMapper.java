package at.backend.MarketingCompany.marketing.campaign.adapter.input.graphql.mapper;

import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivityCost;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivitySchedule;
import at.backend.MarketingCompany.marketing.attribution.adapter.input.graphql.dto.CampaignAttributionResponse;
import at.backend.MarketingCompany.marketing.attribution.core.domain.entity.CampaignAttribution;
import at.backend.MarketingCompany.marketing.campaign.adapter.input.graphql.dto.CampaignOutput;
import at.backend.MarketingCompany.marketing.ab_test.adapter.input.ab.AbTestResponse;
import at.backend.MarketingCompany.marketing.ab_test.core.domain.AbTest;
import at.backend.MarketingCompany.marketing.campaign.core.domain.models.MarketingCampaign;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.*;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.MarketingChannelId;
import at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.dto.CampaignInteractionResponse;
import at.backend.MarketingCompany.marketing.interaction.core.domain.entity.CampaignInteraction;
import at.backend.MarketingCompany.marketing.metric.adapter.input.graphql.dto.CampaignMetricResponse;
import at.backend.MarketingCompany.marketing.metric.core.domain.entity.CampaignMetric;
import at.backend.MarketingCompany.shared.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MarketingOutputMapper {

	public CampaignOutput toOutput(MarketingCampaign domain) {
		if (domain == null) {
			return null;
		}

		var budget = CampaignOutput.CampaignBudget.builder()
				.totalBudget(domain.getBudget() != null ? domain.getBudget().totalBudget() : null)
				.spentAmount(domain.getBudget() != null ? domain.getBudget().spentAmount() : null)
				.remainingBudget(domain.getBudget() != null ? domain.getBudget().remainingBudget() : null)
				.utilizationPercentage(domain.getBudget() != null ? domain.getBudget().utilizationPercentage() : null)
				.isNearlyExhausted(domain.getBudget() != null ? domain.getBudget().isNearlyExhausted() : null)
				.build();

		CampaignOutput.CampaignROI roi = null;

		if (domain.getROIStatus() != null) {
			var ROI = domain.calculateROI();
			roi = CampaignOutput.CampaignROI.builder()
					.totalRevenue(ROI.totalRevenue())
					.totalCost(ROI.totalCost())
					.roiPercentage(ROI.roiPercentage())
					.netProfit(ROI.netProfit())
					.status(ROI.status())
					.isProfitable(ROI.isProfitable())
					.build();
		}

		var period = CampaignOutput.CampaignPeriod.builder()
				.startDate(domain.getPeriod() != null ? domain.getPeriod().startDate() : null)
				.endDate(domain.getPeriod() != null ? domain.getPeriod().endDate() : null)
				.isActive(domain.getPeriod() != null ? domain.getPeriod().isActive() : null)
				.hasStarted(domain.getPeriod() != null ? domain.getPeriod().hasStarted() : null)
				.hasEnded(domain.getPeriod() != null ? domain.getPeriod().hasEnded() : null)
				.durationInDays(domain.getPeriod() != null ? domain.getPeriod().durationInDays() : null)
				.build();

		return CampaignOutput.builder()
				.id(domain.getId() != null ? domain.getId().getValue() : null)
				.name(domain.getName() != null ? domain.getName().value() : null)
				.description(domain.getDescription())
				.campaignType(domain.getCampaignType() != null ? domain.getCampaignType().name() : null)
				.budget(budget)
				.roi(roi)
				.period(period)
				.needsOptimization(domain.needsOptimization())
				.budgetAllocations(domain.getBudgetAllocations())
				.targetAudienceDemographics(domain.getTargetAudienceDemographics())
				.targetLocations(domain.getTargetLocations())
				.targetInterests(domain.getTargetInterests())
				.primaryGoal(domain.getPrimaryGoal())
				.secondaryGoals(domain.getSecondaryGoals())
				.successMetrics(domain.getSuccessMetrics())
				.primaryChannelId(domain.getPrimaryChannelId() != null ? domain.getPrimaryChannelId().getValue() : null)
				.status(domain.getStatus() != null ? domain.getStatus() : null)
				.createdAt(domain.getCreatedAt())
				.updatedAt(domain.getUpdatedAt())
				.build();
	}

	public PageResponse<CampaignOutput> toPageResponse(Page<MarketingCampaign> domainPage) {
		if (domainPage == null) {
			return null;
		}

		return PageResponse.of(domainPage.map(this::toOutput));
	}

	// ========== INTERACTION MAPPING ==========

	public CampaignInteractionResponse toInteractionResponse(CampaignInteraction domain) {
		if (domain == null) {
			return null;
		}

		return new CampaignInteractionResponse(
				domain.getId() != null ? domain.getId().getValue() : null,
				domain.getCampaignId() != null ? domain.getCampaignId().getValue() : null,
				domain.getCustomerId() != null ? domain.getCustomerId().getValue() : null,
				domain.getMarketingInteractionType() != null ? domain.getMarketingInteractionType().name() : null,
				domain.getInteractionDate(),
				domain.getChannelId() != null ? domain.getChannelId().getValue() : null,
				domain.getUtmSource(),
				domain.getUtmMedium(),
				domain.getUtmCampaign(),
				domain.getUtmContent(),
				domain.getUtmTerm(),
				domain.getDeviceType(),
				domain.getDeviceOs(),
				domain.getBrowser(),
				domain.getCountryCode(),
				domain.getCity(),
				domain.getDealId(),
				domain.getConversionValue(),
				domain.isConversion(),
				domain.getLandingPageUrl(),
				domain.getReferrerUrl(),
				domain.getSessionId(),
				domain.getProperties(),
				domain.getCreatedAt()
		);
	}

	public List<CampaignInteractionResponse> toInteractionResponseList(List<CampaignInteraction> domains) {
		if (domains == null) {
			return List.of();
		}
		return domains.stream()
				.map(this::toInteractionResponse)
				.collect(Collectors.toList());
	}

	// ========== METRIC MAPPING ==========

	public CampaignMetricResponse toMetricResponse(CampaignMetric domain) {
		if (domain == null) {
			return null;
		}

		return new CampaignMetricResponse(
				domain.getId() != null ? domain.getId().getValue() : null,
				domain.getCampaignId() != null ? domain.getCampaignId().getValue() : null,
				domain.getName(),
				domain.getMetricType() != null ? domain.getMetricType().name() : null,
				domain.getDescription(),
				domain.getCurrentValue(),
				domain.getTargetValue(),
				domain.getMeasurementUnit(),
				domain.getCalculationFormula(),
				domain.getDataSource(),
				domain.getLastCalculatedDate(),
				domain.isAutomated(),
				domain.isTargetAchieved(),
				domain.achievementPercentage(),
				domain.getCreatedAt(),
				domain.getUpdatedAt()
		);
	}

	public List<CampaignMetricResponse> toMetricResponseList(List<CampaignMetric> domains) {
		if (domains == null) {
			return List.of();
		}
		return domains.stream()
				.map(this::toMetricResponse)
				.collect(Collectors.toList());
	}

	// ========== ATTRIBUTION MAPPING ==========

	public CampaignAttributionResponse toAttributionResponse(CampaignAttribution domain) {
		if (domain == null) {
			return null;
		}

		return new CampaignAttributionResponse(
				domain.getId() != null ? domain.getId().getValue() : null,
				domain.getDealId() != null ? domain.getDealId().getValue() : null,
				domain.getCampaignId() != null ? domain.getCampaignId().getValue() : null,
				domain.getAttributionModel() != null ? domain.getAttributionModel().name() : null,
				domain.getAttributionPercentage(),
				domain.getAttributedRevenue(),
				domain.getTouchTimestamps(),
				domain.getTouchCount(),
				domain.getFirstTouchWeight(),
				domain.getLastTouchWeight(),
				domain.getLinearWeight(),
				domain.getCreatedAt(),
				domain.getUpdatedAt()
		);
	}

	public List<CampaignAttributionResponse> toAttributionResponseList(List<CampaignAttribution> domains) {
		if (domains == null) {
			return List.of();
		}
		return domains.stream()
				.map(this::toAttributionResponse)
				.collect(Collectors.toList());
	}


	public AbTestResponse toAbTestResponse(AbTest domain) {
		if (domain == null) {
			return null;
		}

		return new AbTestResponse(
				domain.getId() != null ? domain.getId().getValue() : null,
				domain.getCampaignId() != null ? domain.getCampaignId().getValue() : null,
				domain.getTestName(),
				domain.getHypothesis(),
				domain.getTestType() != null ? domain.getTestType().name() : null,
				domain.getPrimaryMetric(),
				domain.getConfidenceLevel(),
				domain.getRequiredSampleSize(),
				domain.getControlVariant(),
				domain.getTreatmentVariants(),
				domain.getWinningVariant(),
				domain.getStatisticalSignificance(),
				domain.isCompleted(),
				domain.hasStarted(),
				domain.hasEnded(),
				domain.getStartDate(),
				domain.getEndDate(),
				domain.getCreatedAt(),
				domain.getUpdatedAt()
		);
	}

	public List<AbTestResponse> toAbTestResponseList(List<AbTest> domains) {
		if (domains == null) {
			return List.of();
		}
		return domains.stream()
				.map(this::toAbTestResponse)
				.collect(Collectors.toList());
	}

	public CampaignName toCampaignName(String name) {
		if (name == null || name.isBlank()) {
			return null;
		}
		return new CampaignName(name);
	}

	public CampaignPeriod toCampaignPeriod(java.time.LocalDate startDate, java.time.LocalDate endDate) {
		if (startDate == null) {
			return null;
		}
		return new CampaignPeriod(startDate, endDate);
	}

	public CampaignBudget toCampaignBudget(java.math.BigDecimal totalBudget) {
		if (totalBudget == null) {
			return null;
		}
		return new CampaignBudget(totalBudget, java.math.BigDecimal.ZERO);
	}

	public ActivitySchedule toActivitySchedule(
			java.time.LocalDateTime plannedStart,
			java.time.LocalDateTime plannedEnd,
			java.time.LocalDateTime actualStart,
			java.time.LocalDateTime actualEnd) {

		if (plannedStart == null || plannedEnd == null) {
			return null;
		}
		return new ActivitySchedule(plannedStart, plannedEnd, actualStart, actualEnd);
	}

	public ActivityCost toActivityCost(java.math.BigDecimal plannedCost, java.math.BigDecimal actualCost) {
		if (plannedCost == null) {
			return null;
		}
		return new ActivityCost(plannedCost, actualCost);
	}

	public MarketingChannelId toChannelId(Long id) {
		if (id == null) {
			return null;
		}
		return new MarketingChannelId(id);
	}

	public MarketingCampaignId toCampaignId(Long id) {
		if (id == null) {
			return null;
		}
		return new MarketingCampaignId(id);
	}

	public CustomerCompanyId toCustomerId(Long id) {
		if (id == null) {
			return null;
		}
		return new CustomerCompanyId(id);
	}
}