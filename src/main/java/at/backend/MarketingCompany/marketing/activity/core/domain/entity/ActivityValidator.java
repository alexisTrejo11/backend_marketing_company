package at.backend.MarketingCompany.marketing.activity.core.domain.entity;

import at.backend.MarketingCompany.marketing.activity.core.domain.exception.ActivityBusinessRuleException;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivityCost;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivitySchedule;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivityStatus;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivityType;
import at.backend.MarketingCompany.shared.domain.ValidationResult;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class ActivityValidator {
	private static final int MIN_ADVANCE_HOURS = 1;
	private static final int MAX_ADVANCE_DAYS = 365;
	private static final int TEST_NAME_MIN_LENGTH = 3;
	private static final int TEST_NAME_MAX_LENGTH = 200;
	private static final int VARIANT_NAME_MAX_LENGTH = 200;
	private static final int VARIANT_NAME_MIN_LENGTH = 2;
	private static final int MAX_TEST_DURATION_DAYS = 180;
	private static final int MIN_TEST_DURATION_DAYS = 1;
	private static final BigDecimal MAX_COST_AMOUNT = new BigDecimal("1000000");

	public static ValidationResult validateForCreation(
			ActivitySchedule schedule,
			ActivityCost plannedCost,
			Object campaign,
			String name,
			ActivityType activityType,
			String deliveryChannel) {

		ValidationResult result = new ValidationResult();

		validateSchedule(schedule, result);
		validateCost(plannedCost, result);
		validateCampaign(campaign, result);
		validateName(name, result);
		validateActivityType(activityType, result);
		validateDeliveryChannel(deliveryChannel, result);

		if (result.isValid()) {
			validateBusinessRules(schedule, plannedCost, activityType, result);
		}

		return result;
	}

	private static void validateSchedule(ActivitySchedule schedule, ValidationResult result) {
		if (schedule == null) {
			result.addError("Activity schedule is required");
			return;
		}

		validateScheduleDates(schedule, result);
	}

	private static void validateScheduleDates(ActivitySchedule schedule, ValidationResult result) {
		LocalDateTime now = LocalDateTime.now();

		if (schedule.plannedStartDate() == null) {
			result.addError("Planned start date is required");
		}

		if (schedule.plannedEndDate() == null) {
			result.addError("Planned end date is required");
		}

		if (schedule.plannedStartDate() != null && schedule.plannedEndDate() != null) {
			validateStartDate(schedule.plannedStartDate(), now, result);
			validateDateOrder(schedule.plannedStartDate(), schedule.plannedEndDate(), result);
			validateDuration(schedule.plannedStartDate(), schedule.plannedEndDate(), result);
		}
	}

	private static void validateStartDate(LocalDateTime startDate, LocalDateTime now, ValidationResult result) {
		if (startDate.isBefore(now.plusHours(MIN_ADVANCE_HOURS))) {
			result.addError(String.format("Activities must be planned at least %d hour(s) in advance", MIN_ADVANCE_HOURS));
		}

		if (startDate.isAfter(now.plusDays(MAX_ADVANCE_DAYS))) {
			result.addError(String.format("Activities cannot be planned more than %d days in advance", MAX_ADVANCE_DAYS));
		}
	}

	private static void validateDuration(LocalDateTime startDate, LocalDateTime endDate, ValidationResult result) {
		long durationDays = ChronoUnit.DAYS.between(startDate, endDate);

		if (durationDays < MIN_TEST_DURATION_DAYS) {
			result.addError("Activity must have a minimum duration of 1 day");
		}

		if (durationDays > MAX_TEST_DURATION_DAYS) {
			result.addError(String.format("Test duration cannot exceed %d days", MAX_TEST_DURATION_DAYS));
		}
	}

	private static void validateCost(ActivityCost plannedCost, ValidationResult result) {
		if (plannedCost == null) {
			result.addError("Planned cost is required");
			return;
		}

		if (plannedCost.plannedCost() == null) {
			result.addError("Planned cost amount is required");
			return;
		}

		validateCostAmount(plannedCost.plannedCost(), result);
	}

	private static void validateCostAmount(BigDecimal cost, ValidationResult result) {
		if (cost.compareTo(BigDecimal.ZERO) <= 0) {
			result.addError("Planned cost must be greater than zero");
		}

		if (cost.scale() > 2) {
			result.addError("Planned cost cannot have more than 2 decimal places");
		}

		if (cost.compareTo(MAX_COST_AMOUNT) > 0) {
			result.addError("Planned cost cannot exceed 1,000,000");
		}
	}

	private static void validateCampaign(Object campaign, ValidationResult result) {
		if (campaign == null) {
			result.addError("Campaign is required for activity");
		}
	}

	private static void validateName(String name, ValidationResult result) {
		if (name == null || name.trim().isEmpty()) {
			result.addError("Activity name is required");
			return;
		}

		String trimmed = name.trim();
		if (trimmed.length() < TEST_NAME_MIN_LENGTH) {
			result.addError("Activity name must be at least 3 characters");
		}

		if (trimmed.length() > TEST_NAME_MAX_LENGTH) {
			result.addError("Activity name cannot exceed 200 characters");
		}
	}

	private static void validateActivityType(ActivityType activityType, ValidationResult result) {
		if (activityType == null) {
			result.addError("Activity type is required");
		}
	}

	private static void validateDeliveryChannel(String deliveryChannel, ValidationResult result) {
		if (deliveryChannel != null && deliveryChannel.trim().length() > 100) {
			result.addError("Delivery channel cannot exceed 100 characters");
		}
	}

	private static void validateBusinessRules(
			ActivitySchedule schedule,
			ActivityCost plannedCost,
			ActivityType activityType,
			ValidationResult result) {

		if (schedule.plannedStartDate() != null && schedule.plannedEndDate() != null) {
			validateDurationByActivityType(schedule, activityType, result);
		}

		if (plannedCost.plannedCost() != null) {
			validateCostByActivityType(plannedCost.plannedCost(), activityType, result);
			validateCostDurationRatio(schedule, plannedCost.plannedCost(), result);
		}
	}

	private static void validateDurationByActivityType(
			ActivitySchedule schedule,
			ActivityType activityType,
			ValidationResult result) {

		long durationDays = ChronoUnit.DAYS.between(
				schedule.plannedStartDate(),
				schedule.plannedEndDate()
		);

		long maxDays = getMaxDurationForActivityType(activityType);
		if (durationDays > maxDays) {
			result.addError(String.format(
					"Duration for %s activities cannot exceed %d days",
					activityType, maxDays
			));
		}
	}

	private static long getMaxDurationForActivityType(ActivityType activityType) {
		return switch (activityType) {
			case CONTENT_CREATION, ANALYSIS -> 90;
			case AD_SETUP, SOCIAL_POST -> 30;
			case EMAIL_BLAST, WEBINAR -> 60;
			case EVENT -> 180;
			case OPTIMIZATION -> 120;
			default -> MAX_TEST_DURATION_DAYS;
		};
	}

	private static void validateCostByActivityType(
			BigDecimal plannedCost,
			ActivityType activityType,
			ValidationResult result) {

		BigDecimal maxCost = getMaxCostForActivityType(activityType);
		if (plannedCost.compareTo(maxCost) > 0) {
			result.addError(String.format(
					"Planned cost for %s activities cannot exceed %s",
					activityType, maxCost
			));
		}
	}

	private static BigDecimal getMaxCostForActivityType(ActivityType activityType) {
		// Simplified costs based on actual ActivityType enum
		return switch (activityType) {
			case CONTENT_CREATION -> new BigDecimal("10000.00");
			case AD_SETUP -> new BigDecimal("5000.00");
			case EMAIL_BLAST -> new BigDecimal("8000.00");
			case SOCIAL_POST -> new BigDecimal("3000.00");
			case EVENT -> new BigDecimal("20000.00");
			case WEBINAR -> new BigDecimal("15000.00");
			case ANALYSIS -> new BigDecimal("12000.00");
			case OPTIMIZATION -> new BigDecimal("18000.00");
		};
	}

	private static void validateCostDurationRatio(
			ActivitySchedule schedule,
			BigDecimal plannedCost,
			ValidationResult result) {

		long durationDays = ChronoUnit.DAYS.between(
				schedule.plannedStartDate(),
				schedule.plannedEndDate()
		);

		if (durationDays > 0) {
			BigDecimal costPerDay = plannedCost.divide(
					new BigDecimal(durationDays), 2, RoundingMode.HALF_UP
			);

			if (costPerDay.compareTo(new BigDecimal("10000")) > 0) {
				result.addWarning(String.format(
						"High daily cost detected: %s per day. Consider reviewing the budget.",
						costPerDay
				));
			}
		}
	}

	public static ValidationResult validateForUpdate(
			CampaignActivity activity,
			Map<String, Object> updateParams) {

		ValidationResult result = new ValidationResult();

		if (activity == null) {
			result.addError("Activity cannot be null");
			return result;
		}

		validateStatusForUpdate(activity, result);
		validateRunningActivityUpdate(activity, updateParams, result);

		return result;
	}

	private static void validateStatusForUpdate(CampaignActivity activity, ValidationResult result) {
		if (activity.getStatus() == ActivityStatus.COMPLETED) {
			result.addError("Cannot update a completed activity");
		}

		if (activity.getStatus() == ActivityStatus.CANCELLED) {
			result.addError("Cannot update a cancelled activity");
		}
	}

	private static void validateRunningActivityUpdate(
			CampaignActivity activity,
			Map<String, Object> updateParams,
			ValidationResult result) {

		if (!activity.hasStarted()) {
			return;
		}

		if (updateParams.containsKey("schedule")) {
			result.addError("Cannot change schedule after activity has started");
		}

		if (updateParams.containsKey("activityType")) {
			result.addError("Cannot change activity type after activity has started");
		}
	}

	public static ValidationResult validateCostUpdate(
			CampaignActivity activity,
			BigDecimal actualCost) {

		ValidationResult result = new ValidationResult();

		if (activity == null) {
			result.addError("Activity cannot be null");
			return result;
		}

		validateCostUpdatePreconditions(activity, result);
		validateActualCost(actualCost, result);

		if (result.isValid() && actualCost != null && activity.getCost().plannedCost() != null) {
			validateCostBusinessRules(activity.getCost().plannedCost(), actualCost, result);
		}

		return result;
	}

	private static void validateCostUpdatePreconditions(CampaignActivity activity, ValidationResult result) {
		if (activity.getCost() == null) {
			result.addError("Current cost information is missing");
			return;
		}

		if (activity.getStatus() != ActivityStatus.IN_PROGRESS &&
				activity.getStatus() != ActivityStatus.COMPLETED) {
			result.addError("Can only update cost for in-progress or completed activities");
		}
	}

	private static void validateActualCost(BigDecimal actualCost, ValidationResult result) {
		if (actualCost == null) {
			result.addError("Actual cost cannot be null");
			return;
		}

		if (actualCost.compareTo(BigDecimal.ZERO) < 0) {
			result.addError("Actual cost cannot be negative");
		}

		if (actualCost.scale() > 2) {
			result.addError("Actual cost cannot have more than 2 decimal places");
		}
	}

	private static void validateCostBusinessRules(
			BigDecimal plannedCost,
			BigDecimal actualCost,
			ValidationResult result) {

		BigDecimal maxAllowed = plannedCost.multiply(new BigDecimal("1.20"));
		if (actualCost.compareTo(maxAllowed) > 0) {
			result.addError(String.format(
					"Actual cost cannot exceed planned cost by more than 20%% " +
							"(Planned: %s, Actual: %s, Max allowed: %s)",
					plannedCost, actualCost, maxAllowed
			));
		}

		BigDecimal variancePercentage = calculateCostVariance(plannedCost, actualCost);
		if (variancePercentage != null && variancePercentage.abs().compareTo(new BigDecimal("50")) > 0) {
			result.addWarning(String.format(
					"Extreme cost variance detected: %s%%",
					variancePercentage.setScale(2, RoundingMode.HALF_UP)
			));
		}
	}

	private static BigDecimal calculateCostVariance(BigDecimal planned, BigDecimal actual) {
		if (planned == null || planned.compareTo(BigDecimal.ZERO) == 0) {
			return null;
		}
		return actual.subtract(planned)
				.divide(planned, 4, RoundingMode.HALF_UP)
				.multiply(new BigDecimal("100"));
	}

	public static ValidationResult validateForStart(
			CampaignActivity activity,
			String reason) {

		ValidationResult result = new ValidationResult();

		if (activity == null) {
			result.addError("Activity cannot be null");
			return result;
		}

		validateStartStatus(activity, result);
		validateStartPreconditions(activity, reason, result);

		return result;
	}

	private static void validateStartStatus(CampaignActivity activity, ValidationResult result) {
		if (activity.getStatus() != ActivityStatus.PLANNED) {
			result.addError("Only planned activities can be started");
		}
	}

	private static void validateStartPreconditions(CampaignActivity activity, String reason, ValidationResult result) {
		if (reason == null || reason.trim().isEmpty()) {
			result.addWarning("Consider providing a reason for starting the activity");
		}

		validatePlannedStartDate(activity, result);
		validateCriticalActivityAssignment(activity, result);
	}

	private static void validatePlannedStartDate(CampaignActivity activity, ValidationResult result) {
		if (activity.getSchedule() != null && activity.getSchedule().plannedStartDate() != null) {
			LocalDateTime plannedStart = activity.getSchedule().plannedStartDate();
			if (plannedStart.isBefore(LocalDateTime.now().minusDays(7))) {
				result.addWarning("Activity start is more than 7 days after planned start date");
			}
		}
	}

	private static void validateCriticalActivityAssignment(CampaignActivity activity, ValidationResult result) {
		if (activity.isCritical() && activity.getAssignedToUserId() == null) {
			result.addError("Critical activities must be assigned before starting");
		}
	}

	public static ValidationResult validateForCompletion(
			CampaignActivity activity,
			String reason) {

		ValidationResult result = new ValidationResult();

		if (activity == null) {
			result.addError("Activity cannot be null");
			return result;
		}

		validateCompletionStatus(activity, result);
		validateCompletionReason(reason, result);
		validateSuccessCriteria(activity, result);

		return result;
	}

	private static void validateCompletionStatus(CampaignActivity activity, ValidationResult result) {
		if (activity.getStatus() != ActivityStatus.IN_PROGRESS) {
			result.addError("Only in-progress activities can be completed");
		}
	}

	private static void validateCompletionReason(String reason, ValidationResult result) {
		if (reason == null || reason.trim().isEmpty()) {
			result.addWarning("Consider providing a reason for completing the activity");
		}
	}

	private static void validateSuccessCriteria(CampaignActivity activity, ValidationResult result) {
		if (activity.isCritical() &&
				(activity.getSuccessCriteria() == null ||
						activity.getSuccessCriteria().trim().isEmpty())) {
			result.addWarning("Critical activities should have success criteria defined");
		}
	}

	public static ValidationResult validateForCancellation(
			CampaignActivity activity,
			String reason) {

		ValidationResult result = new ValidationResult();

		if (activity == null) {
			result.addError("Activity cannot be null");
			return result;
		}

		validateCancellationStatus(activity, result);
		validateCancellationReason(reason, result);
		validateInProgressCancellation(activity, reason, result);

		return result;
	}

	private static void validateCancellationStatus(CampaignActivity activity, ValidationResult result) {
		if (activity.getStatus() == ActivityStatus.COMPLETED) {
			result.addError("Cannot cancel a completed activity");
		}

		if (activity.getStatus() == ActivityStatus.CANCELLED) {
			throw new ActivityBusinessRuleException("Activity is already cancelled");
		}
	}

	private static void validateCancellationReason(String reason, ValidationResult result) {
		if (reason == null || reason.trim().isEmpty()) {
			result.addWarning("Consider providing a reason for cancelling the activity");
		}

		if (reason != null && reason.trim().length() > 200) {
			result.addError("Cancellation reason cannot exceed 200 characters");
		}
	}

	private static void validateInProgressCancellation(CampaignActivity activity, String reason, ValidationResult result) {

		if (activity.getStatus() == ActivityStatus.IN_PROGRESS) {
			if (reason == null || reason.trim().isEmpty()) {
				result.addError("A reason is required to cancel an in-progress activity");
			}
		}
	}

	private static void validateDateOrder(LocalDateTime startDate, LocalDateTime endDate, ValidationResult result) {
		if (endDate.isBefore(startDate)) {
			result.addError("Planned end date must be after start date");
		}
	}

	public static ValidationResult validateActivityDates(LocalDateTime actualStartDate, LocalDateTime actualEndDate, ActivitySchedule schedule) {
		ValidationResult result = new ValidationResult();

		if (schedule == null) {
			result.addError("Activity schedule is required for date validation");
			return result;
		}

		if (actualStartDate != null && schedule.plannedStartDate() != null) {
			if (actualStartDate.isBefore(schedule.plannedStartDate())) {
				result.addWarning("Actual start date is before planned start date");
			}
		}

		if (actualEndDate != null && schedule.plannedEndDate() != null) {
			if (actualEndDate.isAfter(schedule.plannedEndDate())) {
				result.addWarning("Actual end date is after planned end date");
			}
		}

		return result;
	}

	public static ValidationResult validateForBlock(CampaignActivity campaignActivity, String reason) {
		ValidationResult result = new ValidationResult();

		if (campaignActivity == null) {
			result.addError("Activity cannot be null");
			return result;
		}

		if (campaignActivity.getStatus() != ActivityStatus.IN_PROGRESS) {
			result.addError("Only in-progress activities can be blocked");
		}

		if (reason == null || reason.trim().isEmpty()) {
			result.addWarning("Consider providing a reason for blocking the activity");
		}

		return result;
	}
}