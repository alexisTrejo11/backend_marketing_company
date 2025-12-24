package at.backend.MarketingCompany.marketing.activity.core.application.service;

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
public class CampaignActivityCommandService implements CampaignActivityCommandServicePort {
  private final ActivityRepositoryPort activityRepository;
  private final CampaignRepositoryPort campaignRepository;

  @Override
  @Transactional
  public CampaignActivity createActivity(CreateActivityCommand command) {
    log.debug("Creating activity for campaign: {}, name: {}",
        command.campaignId().getValue(), command.name());

    // Validate campaign exists
    MarketingCampaign campaign = campaignRepository.findById(command.campaignId())
        .orElseThrow(() -> new BusinessRuleException("Campaign not found"));

    // Validate activity creation
    ActivityValidator.validateForCreation(
        command.plannedStartDate(),
        command.plannedEndDate(),
        command.plannedCost(),
        campaign
    );

    // Create activity using domain factory
    ActivitySchedule schedule = new ActivitySchedule(
        command.plannedStartDate(),
        command.plannedEndDate(),
        null,
        null
    );

    ActivityCost cost = new ActivityCost(command.plannedCost(), null);

    CampaignActivity activity = CampaignActivity.create(
        command.campaignId(),
        command.name(),
        command.activityType(),
        schedule,
        cost,
        command.deliveryChannel()
    );

    // Set optional fields
    if (command.description() != null) {
      activity.setDescription(command.description());
    }
    if (command.assignedToUserId() != null) {
      activity.setAssignedToUserId(command.assignedToUserId());
    }
    if (command.successCriteria() != null) {
      activity.setSuccessCriteria(command.successCriteria());
    }
    if (command.targetAudience() != null) {
      activity.setTargetAudience(command.targetAudience());
    }
    if (command.dependencies() != null) {
      activity.setDependencies(command.dependencies());
    }

    CampaignActivity savedActivity = activityRepository.save(activity);
    log.info("Activity created successfully with ID: {}", savedActivity.getId().getValue());

    return savedActivity;
  }

  @Override
  @Transactional
  public CampaignActivity updateActivity(UpdateActivityCommand command) {
    log.debug("Updating activity: {}", command.activityId().getValue());

    CampaignActivity activity = findActivityByIdOrThrow(command.activityId());

    // Validate update is allowed
    ActivityValidator.validateForUpdate(activity);

    // Update fields
    if (command.name() != null) {
      activity.setName(command.name());
    }
    if (command.description() != null) {
      activity.setDescription(command.description());
    }
    if (command.assignedToUserId() != null) {
      activity.setAssignedToUserId(command.assignedToUserId());
    }
    if (command.successCriteria() != null) {
      activity.setSuccessCriteria(command.successCriteria());
    }
    if (command.targetAudience() != null) {
      activity.setTargetAudience(command.targetAudience());
    }
    if (command.dependencies() != null) {
      activity.setDependencies(command.dependencies());
    }

    CampaignActivity updatedActivity = activityRepository.save(activity);
    log.info("Activity updated successfully: {}", command.activityId().getValue());

    return updatedActivity;
  }

  @Override
  @Transactional
  public CampaignActivity startActivity(CampaignActivityId activityId) {
    log.debug("Starting activity: {}", activityId.getValue());

    CampaignActivity activity = findActivityByIdOrThrow(activityId);

    // Validate start is allowed
    ActivityValidator.validateForStart(activity);

    activity.start();

    CampaignActivity startedActivity = activityRepository.save(activity);
    log.info("Activity started successfully: {}", activityId.getValue());

    return startedActivity;
  }

  @Override
  @Transactional
  public CampaignActivity completeActivity(CampaignActivityId activityId) {
    log.debug("Completing activity: {}", activityId.getValue());

    CampaignActivity activity = findActivityByIdOrThrow(activityId);

    // Validate completion is allowed
    ActivityValidator.validateForCompletion(activity);

    activity.complete();

    CampaignActivity completedActivity = activityRepository.save(activity);
    log.info("Activity completed successfully: {}", activityId.getValue());

    return completedActivity;
  }

  @Override
  @Transactional
  public CampaignActivity cancelActivity(CampaignActivityId activityId) {
    log.debug("Cancelling activity: {}", activityId.getValue());

    CampaignActivity activity = findActivityByIdOrThrow(activityId);

    // Validate cancellation is allowed
    ActivityValidator.validateForCancellation(activity);

    activity.cancel();

    CampaignActivity cancelledActivity = activityRepository.save(activity);
    log.info("Activity cancelled successfully: {}", activityId.getValue());

    return cancelledActivity;
  }

  @Override
  @Transactional
  public CampaignActivity blockActivity(CampaignActivityId activityId, String blockReason) {
    log.debug("Blocking activity: {} with reason: {}", activityId.getValue(), blockReason);

    CampaignActivity activity = findActivityByIdOrThrow(activityId);
    activity.block();

    CampaignActivity blockedActivity = activityRepository.save(activity);
    log.info("Activity blocked successfully: {}", activityId.getValue());

    return blockedActivity;
  }

  @Override
  @Transactional
  public CampaignActivity updateActivityCost(UpdateActivityCostCommand command) {
    log.debug("Updating activity cost: {} to {}",
        command.activityId().getValue(), command.actualCost());

    CampaignActivity activity = findActivityByIdOrThrow(command.activityId());

    // Validate cost update
    ActivityValidator.validateCostUpdate(activity, command.actualCost());

    // Update cost using domain logic
    ActivityCost currentCost = activity.getCost();
    ActivityCost newCost = new ActivityCost(currentCost.plannedCost(), command.actualCost());
    activity.setCost(newCost);

    CampaignActivity updatedActivity = activityRepository.save(activity);
    log.info("Activity cost updated successfully: {}", command.activityId().getValue());

    return updatedActivity;
  }

  @Override
  @Transactional
  public CampaignActivity recordActivityDates(RecordActivityDatesCommand command) {
    log.debug("Recording activity dates: {}", command.activityId().getValue());

    CampaignActivity activity = findActivityByIdOrThrow(command.activityId());

    ActivitySchedule currentSchedule = activity.getSchedule();
    ActivitySchedule newSchedule = new ActivitySchedule(
        currentSchedule.plannedStartDate(),
        currentSchedule.plannedEndDate(),
        command.actualStartDate(),
        command.actualEndDate()
    );

    activity.setSchedule(newSchedule);

    CampaignActivity updatedActivity = activityRepository.save(activity);
    log.info("Activity dates recorded successfully: {}", command.activityId().getValue());

    return updatedActivity;
  }

  @Override
  @Transactional
  public CampaignActivity assignToUser(CampaignActivityId activityId, Long userId) {
    log.debug("Assigning activity {} to user: {}", activityId.getValue(), userId);

    CampaignActivity activity = findActivityByIdOrThrow(activityId);
    activity.setAssignedToUserId(userId);

    CampaignActivity assignedActivity = activityRepository.save(activity);
    log.info("Activity assigned successfully: {}", activityId.getValue());

    return assignedActivity;
  }

  @Override
  @Transactional
  public void deleteActivity(CampaignActivityId activityId) {
    log.debug("Deleting activity: {}", activityId.getValue());

    CampaignActivity activity = findActivityByIdOrThrow(activityId);

    if (activity.getStatus() == ActivityStatus.IN_PROGRESS) {
      throw new BusinessRuleException(
          "Cannot delete an activity that is in progress. " +
              "Please cancel or complete it first."
      );
    }

    activity.softDelete();
    activityRepository.save(activity);

    log.info("Activity deleted successfully: {}", activityId.getValue());
  }

  private CampaignActivity findActivityByIdOrThrow(CampaignActivityId activityId) {
    return activityRepository.findById(activityId)
        .orElseThrow(() -> new BusinessRuleException(
            "Activity not found with ID: " + activityId.getValue()
        ));
  }
}