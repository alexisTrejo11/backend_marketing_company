package at.backend.MarketingCompany.marketing.activity.core.application.service;

import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.UserId;
import at.backend.MarketingCompany.marketing.activity.core.application.command.CreateActivityCommand;
import at.backend.MarketingCompany.marketing.activity.core.application.command.RecordActivityDatesCommand;
import at.backend.MarketingCompany.marketing.activity.core.application.command.UpdateActivityCommand;
import at.backend.MarketingCompany.marketing.activity.core.application.command.UpdateActivityCostCommand;
import at.backend.MarketingCompany.marketing.activity.core.application.dto.ActivityStatistics;
import at.backend.MarketingCompany.marketing.activity.core.application.query.ActivityQuery;
import at.backend.MarketingCompany.marketing.activity.core.domain.entity.ActivityValidator;
import at.backend.MarketingCompany.marketing.activity.core.domain.entity.CampaignActivity;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivityCost;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivitySchedule;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivityStatus;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.CampaignActivityId;
import at.backend.MarketingCompany.marketing.activity.core.port.input.CampaignActivityCommandServicePort;
import at.backend.MarketingCompany.marketing.activity.core.port.input.CampaignActivityQueryServicePort;
import at.backend.MarketingCompany.marketing.activity.core.port.output.ActivityRepositoryPort;
import at.backend.MarketingCompany.marketing.campaign.core.domain.models.MarketingCampaign;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.ports.output.CampaignRepositoryPort;
import at.backend.MarketingCompany.shared.exception.BusinessRuleException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CampaignActivityQueryService implements CampaignActivityQueryServicePort {
  private final ActivityRepositoryPort activityRepository;
  private final CampaignRepositoryPort campaignRepository;

  @Override
  @Transactional(readOnly = true)
  public CampaignActivity getActivityById(CampaignActivityId activityId) {
    return activityRepository.findById(activityId)
        .orElseThrow(() -> new BusinessRuleException(
            "Activity with ID " + activityId.getValue() + " not found."
        ));
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CampaignActivity> searchActivities(ActivityQuery query, Pageable pageable) {
    if (query.isEmpty()) {
      return activityRepository.findAll(pageable);
    }

    return activityRepository.findByFilters(
        query.campaignId(),
        query.statuses(),
        query.activityTypes(),
        query.assignedToUserId(),
        query.plannedStartFrom(),
        query.plannedStartTo(),
        query.isDelayed(),
        query.isCompleted(),
        query.searchTerm(),
        pageable
    );
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CampaignActivity> getActivitiesByCampaign(MarketingCampaignId campaignId, Pageable pageable) {
    return activityRepository.findByCampaignId(campaignId, pageable);
  }


  @Override
  @Transactional(readOnly = true)
  public Page<CampaignActivity> getActivitiesByStatus(
      MarketingCampaignId campaignId,
      ActivityStatus status,
      Pageable pageable) {

    return activityRepository.findByCampaignIdAndStatus(campaignId, status, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CampaignActivity> getActivitiesByUser(UserId userId, Pageable pageable) {
    return activityRepository.findByAssignedUserId(userId, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CampaignActivity> getDelayedActivities(Pageable pageable) {
    return activityRepository.findDelayedActivities(pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CampaignActivity> getOverBudgetActivities(Pageable pageable) {
    return activityRepository.findOverBudgetActivities(pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CampaignActivity> getUpcomingActivities(LocalDateTime fromDate, Pageable pageable) {

    return activityRepository.findUpcomingActivities(fromDate, pageable);
  }
}