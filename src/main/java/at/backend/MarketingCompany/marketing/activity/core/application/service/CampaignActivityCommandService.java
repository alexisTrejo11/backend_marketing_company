package at.backend.MarketingCompany.marketing.activity.core.application.service;

import at.backend.MarketingCompany.marketing.activity.core.application.command.CreateActivityCommand;
import at.backend.MarketingCompany.marketing.activity.core.application.command.RecordActivityDatesCommand;
import at.backend.MarketingCompany.marketing.activity.core.application.command.UpdateActivityCommand;
import at.backend.MarketingCompany.marketing.activity.core.application.command.UpdateActivityCostCommand;
import at.backend.MarketingCompany.marketing.activity.core.domain.entity.CampaignActivity;
import at.backend.MarketingCompany.marketing.activity.core.domain.entity.CreateActivityParams;
import at.backend.MarketingCompany.marketing.activity.core.domain.exception.CampaignActivityNotFoundException;
import at.backend.MarketingCompany.marketing.activity.core.domain.exception.CampaignActivityValidationException;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivityStatus;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.CampaignActivityId;
import at.backend.MarketingCompany.marketing.activity.core.port.input.CampaignActivityCommandServicePort;
import at.backend.MarketingCompany.marketing.activity.core.port.output.ActivityRepositoryPort;
import at.backend.MarketingCompany.marketing.campaign.core.ports.output.CampaignRepositoryPort;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class CampaignActivityCommandService implements CampaignActivityCommandServicePort {
	private final ActivityRepositoryPort activityRepository;
	private final CampaignRepositoryPort campaignRepository;

	@Override
	@Transactional
	public CampaignActivity createActivity(CreateActivityCommand command) {
		log.debug("Creating activity for campaign: {}, name: {}", command.campaignId().getValue(), command.name());

		if (!campaignRepository.existsById(command.campaignId())) {
			throw new CampaignActivityValidationException("Campaign not found");
		}

		CreateActivityParams params = command.toCreateActivityParams();
		CampaignActivity activity = CampaignActivity.create(params);

		CampaignActivity savedActivity = activityRepository.save(activity);
		log.info("Activity created successfully with ID: {}", savedActivity.getId().getValue());

		return savedActivity;
	}

	@Override
	@Transactional
	public CampaignActivity updateActivity(UpdateActivityCommand command) {
		log.debug("Updating activity: {}", command.activityId().getValue());

		CampaignActivity activity = findActivityByIdOrThrow(command.activityId());
		activity.updateGeneralInfo(
				command.name(),
				command.description(),
				command.successCriteria(),
				command.targetAudience(),
				command.dependencies()
		);


		CampaignActivity updatedActivity = activityRepository.save(activity);
		log.info("Activity updated successfully: {}", command.activityId().getValue());

		return updatedActivity;
	}

	@Override
	@Transactional
	public CampaignActivity startActivity(CampaignActivityId activityId) {
		log.debug("Starting activity: {}", activityId.getValue());

		CampaignActivity activity = findActivityByIdOrThrow(activityId);
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
		log.debug("Updating activity cost: {} to {}", command.activityId().getValue(), command.actualCost());

		CampaignActivity activity = findActivityByIdOrThrow(command.activityId());
		activity.updateActualCost(command.actualCost());

		CampaignActivity updatedActivity = activityRepository.save(activity);
		log.info("Activity cost updated successfully: {}", command.activityId().getValue());

		return updatedActivity;
	}

	@Override
	@Transactional
	public CampaignActivity recordActivityDates(RecordActivityDatesCommand command) {
		log.debug("Recording activity dates: {}", command.activityId().getValue());

		CampaignActivity activity = findActivityByIdOrThrow(command.activityId());
		activity.recordActivityDates(command.actualStartDate(), command.actualEndDate());

		CampaignActivity updatedActivity = activityRepository.save(activity);
		log.info("Activity dates recorded successfully: {}", command.activityId().getValue());

		return updatedActivity;
	}

	@Override
	@Transactional
	public CampaignActivity assignToUser(CampaignActivityId activityId, Long userId) {
		log.debug("Assigning activity {} to user: {}", activityId.getValue(), userId);

		CampaignActivity activity = findActivityByIdOrThrow(activityId);
		activity.assign(userId);

		CampaignActivity assignedActivity = activityRepository.save(activity);
		log.info("Activity assigned successfully: {}", activityId.getValue());

		return assignedActivity;
	}

	@Override
	public CampaignActivity unassign(CampaignActivityId activityId) {
		log.debug("Unassigning activity: {}", activityId.getValue());

		CampaignActivity activity = findActivityByIdOrThrow(activityId);
		activity.unassign();

		CampaignActivity unassignedActivity = activityRepository.save(activity);
		log.info("Activity unassigned successfully: {}", activityId.getValue());

		return unassignedActivity;
	}

	@Override
	@Transactional
	public void deleteActivity(CampaignActivityId activityId) {
		log.debug("Deleting activity: {}", activityId.getValue());

		CampaignActivity activity = findActivityByIdOrThrow(activityId);

		if (activity.getStatus() == ActivityStatus.IN_PROGRESS) {
			throw new CampaignActivityValidationException(
					"Cannot delete an activity that is in progress. Please cancel or complete it first."
			);
		}

		activity.softDelete();
		activityRepository.save(activity);

		log.info("Activity deleted successfully: {}", activityId.getValue());
	}

	private CampaignActivity findActivityByIdOrThrow(CampaignActivityId activityId) {
		return activityRepository.findById(activityId)
				.orElseThrow(() -> new CampaignActivityNotFoundException(activityId));
		}
}