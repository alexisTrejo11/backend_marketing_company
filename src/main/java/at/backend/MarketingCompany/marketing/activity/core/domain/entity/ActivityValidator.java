package at.backend.MarketingCompany.marketing.activity.core.domain.entity;

import at.backend.MarketingCompany.marketing.activity.core.domain.exception.CampaignActivityBusinessRuleException;
import at.backend.MarketingCompany.marketing.activity.core.domain.exception.CampaignActivityValidationException;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivityCost;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivitySchedule;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivityStatus;
import at.backend.MarketingCompany.marketing.campaign.core.domain.exception.MarketingDomainException;
import at.backend.MarketingCompany.shared.exception.BusinessRuleException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ActivityValidator {

	private static final int MIN_ADVANCE_HOURS = 1;

	public static void validateForCreation(
			ActivitySchedule schedule,
			ActivityCost plannedCost,
			Object campaign) {

		if (schedule == null) {
			throw new CampaignActivityValidationException("Activity schedule is required");
		}

		if (schedule.actualStartDate() == null) {
			throw new CampaignActivityValidationException("Planned start date is required");
		}

		if (schedule.plannedEndDate() == null) {
			throw new CampaignActivityValidationException("Planned end date is required");
		}

		if (schedule.plannedStartDate().isBefore(LocalDateTime.now().plusHours(MIN_ADVANCE_HOURS))) {
			throw new CampaignActivityValidationException(
					String.format("Activities must be planned at least %d hour(s) in advance",
							MIN_ADVANCE_HOURS)
			);
		}

		if (schedule.plannedEndDate().isBefore(schedule.plannedStartDate())) {
			throw new CampaignActivityValidationException("Planned end date must be after start date");
		}

		if (plannedCost == null) {
			throw new CampaignActivityValidationException("Planned cost is required");
		}

		if (plannedCost.plannedCost().compareTo(BigDecimal.ZERO) <= 0) {
			throw new CampaignActivityValidationException("Planned cost must be greater than zero");
		}

		if (campaign == null) {
			throw new CampaignActivityValidationException("Campaign is required for activity");
		}
	}

	public static void validateForUpdate(CampaignActivity activity) {
		if (activity == null) {
			throw new CampaignActivityValidationException("Activity cannot be null");
		}

		if (activity.getStatus() == ActivityStatus.COMPLETED) {
			throw new CampaignActivityBusinessRuleException("Cannot update a completed activity");
		}

		if (activity.getStatus() == ActivityStatus.CANCELLED) {
			throw new CampaignActivityBusinessRuleException("Cannot update a cancelled activity");
		}
	}

	public static void validateCostUpdate(
			CampaignActivity activity,
			BigDecimal actualCost) {

		if (activity == null) {
			throw new CampaignActivityValidationException("Activity cannot be null");
		}

		if (activity.getCost() == null) {
			throw new MarketingDomainException("Current cost information is missing");
		}

		if (activity.getStatus() != ActivityStatus.IN_PROGRESS &&
				activity.getStatus() != ActivityStatus.COMPLETED) {
			throw new CampaignActivityBusinessRuleException(
					"Can only update cost for in-progress or completed activities"
			);
		}

		if (actualCost == null || actualCost.compareTo(BigDecimal.ZERO) < 0) {
			throw new CampaignActivityValidationException("Actual cost cannot be negative");
		}
	}

	public static void validateForStart(CampaignActivity activity) {
		if (activity == null) {
			throw new CampaignActivityValidationException("Activity cannot be null");
		}

		if (activity.getStatus() != ActivityStatus.PLANNED) {
			throw new CampaignActivityBusinessRuleException("Only planned activities can be started");
		}
	}

	public static void validateForCompletion(CampaignActivity activity) {
		if (activity == null) {
			throw new CampaignActivityValidationException("Activity cannot be null");
		}

		if (activity.getStatus() != ActivityStatus.IN_PROGRESS) {
			throw new CampaignActivityBusinessRuleException("Only in-progress activities can be completed");
		}
	}

	public static void validateForCancellation(CampaignActivity activity) {
		if (activity == null) {
			throw new CampaignActivityValidationException("Activity cannot be null");
		}

		if (activity.getStatus() == ActivityStatus.COMPLETED) {
			throw new CampaignActivityBusinessRuleException("Cannot cancel a completed activity");
		}

		if (activity.getStatus() == ActivityStatus.CANCELLED) {
			throw new CampaignActivityBusinessRuleException("Activity is already cancelled");
		}
	}
}