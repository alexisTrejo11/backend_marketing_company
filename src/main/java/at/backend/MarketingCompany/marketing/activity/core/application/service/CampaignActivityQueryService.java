package at.backend.MarketingCompany.marketing.activity.core.application.service;

import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.UserId;
import at.backend.MarketingCompany.marketing.activity.core.application.query.ActivityQuery;
import at.backend.MarketingCompany.marketing.activity.core.domain.entity.ActivityValidator;
import at.backend.MarketingCompany.marketing.activity.core.domain.entity.CampaignActivity;
import at.backend.MarketingCompany.marketing.activity.core.domain.exception.CampaignActivityNotFoundException;
import at.backend.MarketingCompany.marketing.activity.core.domain.exception.ActivityValidationException;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivityStatus;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.CampaignActivityId;
import at.backend.MarketingCompany.marketing.activity.core.port.input.CampaignActivityQueryServicePort;
import at.backend.MarketingCompany.marketing.activity.core.port.output.ActivityRepositoryPort;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.ports.output.CampaignRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        .orElseThrow(() -> new CampaignActivityNotFoundException(activityId));
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
    if (!campaignRepository.existsById(campaignId)) {
      throw new ActivityValidationException(campaignId);
    }

    return activityRepository.findByCampaignId(campaignId, pageable);
  }


  @Override
  @Transactional(readOnly = true)
  public Page<CampaignActivity> getActivitiesByStatus(
      MarketingCampaignId campaignId, ActivityStatus status, Pageable pageable) {
    if (!campaignRepository.existsById(campaignId)) {
      throw new ActivityValidationException(campaignId);
    }

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