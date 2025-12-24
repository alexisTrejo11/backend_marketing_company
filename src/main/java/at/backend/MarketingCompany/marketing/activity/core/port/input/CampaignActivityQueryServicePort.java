package at.backend.MarketingCompany.marketing.activity.core.port.input;

import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.UserId;
import at.backend.MarketingCompany.marketing.activity.core.application.query.ActivityQuery;
import at.backend.MarketingCompany.marketing.activity.core.domain.entity.CampaignActivity;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivityStatus;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.CampaignActivityId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface CampaignActivityQueryServicePort {

	/**
	 * Retrieves an activity by ID
	 */
	CampaignActivity getActivityById(CampaignActivityId activityId);

	/**
	 * Searches activities with filters
	 */
	Page<CampaignActivity> searchActivities(ActivityQuery query, Pageable pageable);

	/**
	 * Retrieves activities by campaign
	 */
	Page<CampaignActivity> getActivitiesByCampaign(MarketingCampaignId campaignId, Pageable pageable);

	/**
	 * Retrieves activities by status
	 */
	Page<CampaignActivity> getActivitiesByStatus(
			MarketingCampaignId campaignId,
			ActivityStatus status,
			Pageable pageable
	);

	/**
	 * Retrieves upcoming activities
	 */
	Page<CampaignActivity> getUpcomingActivities(
			LocalDateTime fromDate,
			Pageable pageable
	);

	/**
	 * Retrieves activities by assigned user
	 */
	Page<CampaignActivity> getActivitiesByUser(UserId userId, Pageable pageable);

	/**
	 * Retrieves delayed activities
	 */
	Page<CampaignActivity> getDelayedActivities(Pageable pageable);

	/**
	 * Retrieves over-budget activities
	 */
	Page<CampaignActivity> getOverBudgetActivities(Pageable pageable);

}
