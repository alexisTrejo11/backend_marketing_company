package at.backend.MarketingCompany.marketing.activity.core.application.service;

import at.backend.MarketingCompany.marketing.activity.core.application.dto.ActivityStatistics;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivityStatus;
import at.backend.MarketingCompany.marketing.activity.core.port.input.CampaignActivityStatisticsServicePort;
import at.backend.MarketingCompany.marketing.activity.core.port.output.ActivityRepositoryPort;
import at.backend.MarketingCompany.marketing.campaign.core.domain.models.MarketingCampaign;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.ports.output.CampaignRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
public class CampaignActivityStatisticsService implements CampaignActivityStatisticsServicePort {
	private final ActivityRepositoryPort activityRepository;
	private final CampaignRepositoryPort campaignRepository;

	@Override
	@Transactional(readOnly = true)
	public ActivityStatistics getActivityStatistics(MarketingCampaignId campaignId) {
		log.debug("Getting activity statistics for campaign: {}", campaignId);

		Long totalActivities = activityRepository.countByCampaignId(campaignId);
		Long plannedActivities = activityRepository.countByCampaignIdAndStatus(
				campaignId, ActivityStatus.PLANNED
		);
		Long inProgressActivities = activityRepository.countByCampaignIdAndStatus(
				campaignId, ActivityStatus.IN_PROGRESS
		);
		Long completedActivities = activityRepository.countByCampaignIdAndStatus(
				campaignId, ActivityStatus.COMPLETED
		);
		Long cancelledActivities = activityRepository.countByCampaignIdAndStatus(
				campaignId, ActivityStatus.CANCELLED
		);
		Long blockedActivities = activityRepository.countByCampaignIdAndStatus(
				campaignId, ActivityStatus.BLOCKED
		);

		Long delayedActivities = activityRepository.countDelayedActivitiesByCampaignId(campaignId);

		BigDecimal totalPlannedCost = activityRepository
				.calculateTotalPlannedCostByCampaignId(campaignId);
		BigDecimal totalActualCost = activityRepository
				.calculateTotalActualCostByCampaignId(campaignId);
		BigDecimal averageCostOverrun = activityRepository
				.calculateAverageCostOverrunByCampaignId(campaignId);

		Double completionRate = 0d; //calculateCompletionRate(totalActivities, completedActivities);
		Double onTimeRate = activityRepository
				.calculateOnTimeCompletionRateByCampaignId(campaignId);

		ActivityStatistics.ActivityTypeBreakdown typeBreakdown =
				buildTypeBreakdown(campaignId);
		ActivityStatistics.ActivityTimeMetrics timeMetrics =
				buildTimeMetrics(campaignId);

		MarketingCampaign campaign = campaignRepository.findById(
				campaignId
		).orElse(null);

		return ActivityStatistics.builder()
				.campaignId(campaignId.getValue())
				.campaignName(campaign != null ? campaign.getName().value() : null)
				.totalActivities(totalActivities)
				.plannedActivities(plannedActivities)
				.inProgressActivities(inProgressActivities)
				.completedActivities(completedActivities)
				.cancelledActivities(cancelledActivities)
				.blockedActivities(blockedActivities)
				.delayedActivities(delayedActivities)
				.totalPlannedCost(totalPlannedCost)
				.totalActualCost(totalActualCost)
				.averageCostOverrun(averageCostOverrun)
				.completionRate(completionRate)
				.onTimeCompletionRate(onTimeRate)
				.typeBreakdown(typeBreakdown)
				.timeMetrics(timeMetrics)
				.build();
	}

	@Override
	public Double getCompletionRate(MarketingCampaignId campaignId) {
		return 0.0;
	}

	@Override
	public BigDecimal getAverageCostOverrun(MarketingCampaignId campaignId) {
		return null;
	}

	public Double getCompletionRate(Long campaignId) {
		return 0.0;
	}

	public BigDecimal getAverageCostOverrun(Long campaignId) {
		return null;
	}

	public ActivityStatistics.ActivityTypeBreakdown buildTypeBreakdown(MarketingCampaignId campaignId) {
		return null;
	}


	public ActivityStatistics.ActivityTimeMetrics buildTimeMetrics(MarketingCampaignId campaignId) {
		return null;
	}
}
