package at.backend.MarketingCompany.marketing.activity.adapter.input.graphql.controller;

import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.UserId;
import at.backend.MarketingCompany.marketing.activity.adapter.input.graphql.dto.input.SearchActivityInput;
import at.backend.MarketingCompany.marketing.activity.adapter.input.graphql.dto.output.CampaignActivityOutput;
import at.backend.MarketingCompany.marketing.activity.adapter.input.graphql.mapper.CampaignActivityOutputMapper;
import at.backend.MarketingCompany.marketing.activity.core.application.dto.ActivityStatistics;
import at.backend.MarketingCompany.marketing.activity.core.application.query.ActivityQuery;
import at.backend.MarketingCompany.marketing.activity.core.domain.entity.CampaignActivity;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivityStatus;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.CampaignActivityId;
import at.backend.MarketingCompany.marketing.activity.core.port.input.CampaignActivityQueryServicePort;
import at.backend.MarketingCompany.marketing.activity.core.port.input.CampaignActivityStatisticsServicePort;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.shared.PageResponse;
import at.backend.MarketingCompany.shared.dto.PageInput;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ActivityQueryController {
	private final CampaignActivityQueryServicePort activityQueryService;
	private final CampaignActivityStatisticsServicePort statisticsService;
	private final CampaignActivityOutputMapper outputMapper;

	@QueryMapping
	public CampaignActivityOutput campaignActivity(@Argument @Valid @NotNull Long activityId) {
		var activityIdObj = new CampaignActivityId(activityId);
		CampaignActivity activity = activityQueryService.getActivityById(activityIdObj);

		return outputMapper.toOutput(activity);
	}

	@QueryMapping
	public PageResponse<CampaignActivityOutput> searchCampaignActivities(
			@Argument @Valid @NotNull SearchActivityInput filter,
			@Argument @Valid @NotNull PageInput pageInput) {
		ActivityQuery query = filter.toQuery();
		Pageable pageable = pageInput.toPageable();

		var activityPage = activityQueryService.searchActivities(query, pageable);
		return outputMapper.toPageResponse(activityPage);
	}

	@QueryMapping
	public PageResponse<CampaignActivityOutput> campaignActivitiesByCampaignAndByStatus(
			@Argument @Valid @NotNull Long campaignId,
			@Argument @Valid @NotNull ActivityStatus status,
			@Argument @Valid @NotNull PageInput pageInput) {
		Pageable pageable = pageInput.toPageable();
		var activityIdObj = new MarketingCampaignId(campaignId);

		var activityPage = activityQueryService.getActivitiesByStatus(activityIdObj, status, pageable);
		return outputMapper.toPageResponse(activityPage);
	}

	@QueryMapping
	public PageResponse<CampaignActivityOutput> campaignActivitiesByUser(
			@Argument @Valid @NotNull Long userId,
			@Argument @Valid @NotNull PageInput pageInput) {
		Pageable pageable = pageInput.toPageable();
		UserId userIdObj = new UserId(userId);

		Page<CampaignActivity> activityPage = activityQueryService.getActivitiesByUser(userIdObj, pageable);
		return outputMapper.toPageResponse(activityPage);
	}

	@QueryMapping
	public PageResponse<CampaignActivityOutput> delayedCampaignActivities(
			@Argument @Valid @NotNull PageInput pageInput) {
		Pageable pageable = pageInput.toPageable();

		Page<CampaignActivity> activityPage = activityQueryService.getDelayedActivities(pageable);
		return outputMapper.toPageResponse(activityPage);
	}

	@QueryMapping
	public PageResponse<CampaignActivityOutput> overBudgetCampaignActivities(
			@Argument @Valid @NotNull PageInput pageInput) {
		Pageable pageable = pageInput.toPageable();

		Page<CampaignActivity> activityPage = activityQueryService.getOverBudgetActivities(pageable);
		return outputMapper.toPageResponse(activityPage);
	}

	@QueryMapping
	public PageResponse<CampaignActivityOutput> upcomingCampaignActivities(
			@Argument @Valid @NotNull PageInput pageInput) {
		Pageable pageable = pageInput.toPageable();

		LocalDateTime aWeekFromNow = LocalDateTime.now().plusWeeks(1);
		Page<CampaignActivity> activityPage = activityQueryService.getUpcomingActivities(aWeekFromNow, pageable);
		return outputMapper.toPageResponse(activityPage);
	}

	@QueryMapping
	public ActivityStatistics campaignActivityStatistics(
			@Argument @Valid @NotNull Long campaignId) {
		var campaignIdObj = new MarketingCampaignId(campaignId);
		return statisticsService.getActivityStatistics(campaignIdObj);
	}
}
