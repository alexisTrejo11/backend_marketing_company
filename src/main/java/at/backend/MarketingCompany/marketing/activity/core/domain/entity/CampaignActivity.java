package at.backend.MarketingCompany.marketing.activity.core.domain.entity;

import at.backend.MarketingCompany.marketing.activity.core.domain.exception.ActivityCostException;
import at.backend.MarketingCompany.marketing.activity.core.domain.exception.*;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.*;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.shared.domain.BaseDomainEntity;
import at.backend.MarketingCompany.shared.domain.ValidationResult;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Objects;

@Getter
@Slf4j
public class CampaignActivity extends BaseDomainEntity<CampaignActivityId> {
	private MarketingCampaignId campaignId;
	private String name;
	private ActivityType activityType;
	private ActivityStatus status;
	private ActivitySchedule schedule;
	private ActivityCost cost;
	private String deliveryChannel;
	private Long assignedToUserId;
	private String description;
	private String successCriteria;
	private String targetAudience;
	private Object dependencies;

	private LocalDateTime statusChangedAt;
	private String lastStatusChangeReason;

	private CampaignActivity() {
		this.status = ActivityStatus.PLANNED;
		this.statusChangedAt = LocalDateTime.now();
	}

	public CampaignActivity(CampaignActivityId id) {
		super(id);
		this.status = ActivityStatus.PLANNED;
		this.statusChangedAt = LocalDateTime.now();
	}

	public static CampaignActivity create(CreateActivityParams params) {
		ValidationResult result = ActivityValidator.validateForCreation(params.schedule(), params.cost(), params.campaignId(), params.name(), params.activityType(), params.deliveryChannel());
		if (!result.isValid()) {
			throw new ActivityValidationException(result.getErrorsAsString());
		}

		CampaignActivity activity = new CampaignActivity(CampaignActivityId.generate());
		initializeActivityFromParams(activity, params);
		activity.validateBusinessRules();

		return activity;
	}

	private static void initializeActivityFromParams(CampaignActivity activity, CreateActivityParams params) {
		activity.campaignId = params.campaignId();
		activity.name = params.name().trim();
		activity.activityType = params.activityType();
		activity.schedule = params.schedule();
		activity.cost = params.cost();
		activity.deliveryChannel = params.deliveryChannel() != null ? params.deliveryChannel().trim() : null;
		activity.status = ActivityStatus.PLANNED;
		activity.dependencies = params.dependencies();
		activity.description = params.description() != null ? params.description().trim() : null;
		activity.successCriteria = params.successCriteria() != null ? params.successCriteria().trim() : null;
		activity.targetAudience = params.targetAudience() != null ? params.targetAudience().trim() : null;
		activity.statusChangedAt = LocalDateTime.now();
	}

	private void validateBusinessRules() {
		validateCostByActivityType();
		validateDurationByActivityType();
		validateCriticalActivityRequirements();
	}

	private void validateCostByActivityType() {
		if (cost == null || cost.plannedCost() == null) {
			return;
		}

		BigDecimal maxCost = getMaxCostForActivityType();
		if (cost.plannedCost().compareTo(maxCost) > 0) {
			throw new ActivityCostException(String.format("Planned cost for %s activities cannot exceed %s", activityType, maxCost));
		}
	}

	private BigDecimal getMaxCostForActivityType() {
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

	private void validateDurationByActivityType() {
		if (schedule == null || schedule.plannedStartDate() == null || schedule.plannedEndDate() == null) {
			return;
		}

		long daysBetween = ChronoUnit.DAYS.between(schedule.plannedStartDate(), schedule.plannedEndDate());

		long maxDays = getMaxDurationForActivityType();
		if (daysBetween > maxDays) {
			throw new ActivityScheduleException(String.format("Duration for %s activities cannot exceed %d days", activityType, maxDays));
		}

		if (daysBetween < 1) {
			throw new ActivityScheduleException("Activity must have a minimum duration of 1 day");
		}
	}

	private long getMaxDurationForActivityType() {
		return switch (activityType) {
			case CONTENT_CREATION, ANALYSIS -> 90;
			case AD_SETUP, SOCIAL_POST -> 30;
			case EMAIL_BLAST, WEBINAR -> 60;
			case EVENT -> 180;
			case OPTIMIZATION -> 120;
		};
	}

	private void validateCriticalActivityRequirements() {
		if (!isCriticalActivity()) {
			return;
		}

		if (successCriteria == null || successCriteria.trim().isEmpty()) {
			throw new ActivityBusinessRuleException("Success criteria is required for critical activities");
		}

		if (targetAudience == null || targetAudience.trim().isEmpty()) {
			throw new ActivityBusinessRuleException("Target audience is required for critical activities");
		}

		if (assignedToUserId == null) {
			throw new ActivityBusinessRuleException("Critical activities must be assigned to a user");
		}
	}

	private boolean isCriticalActivity() {
		return activityType == ActivityType.EVENT || activityType == ActivityType.WEBINAR || (cost != null && cost.plannedCost() != null && cost.plannedCost().compareTo(new BigDecimal("10000.00")) > 0);
	}

	public static CampaignActivity reconstruct(CampaignActivityReconstructParams params) {
		if (params == null) {
			return null;
		}

		CampaignActivity activity = new CampaignActivity();
		populateReconstructedFields(activity, params);
		return activity;
	}

	private static void populateReconstructedFields(CampaignActivity activity, CampaignActivityReconstructParams params) {
		activity.id = params.id();
		activity.campaignId = params.campaignId();
		activity.name = params.name();
		activity.description = params.description();
		activity.activityType = params.activityType();
		activity.status = params.status() != null ? params.status() : ActivityStatus.PLANNED;
		activity.schedule = params.schedule();
		activity.cost = params.cost();
		activity.assignedToUserId = params.assignedToUserId();
		activity.deliveryChannel = params.deliveryChannel();
		activity.successCriteria = params.successCriteria();
		activity.targetAudience = params.targetAudience();
		activity.dependencies = params.dependencies();
		activity.statusChangedAt = params.statusChangedAt();
		activity.lastStatusChangeReason = params.lastStatusChangeReason();
		activity.createdAt = params.createdAt();
		activity.updatedAt = params.updatedAt();
		activity.deletedAt = params.deletedAt();
		activity.version = params.version();
	}

	public void start(String reason) {
		validateStatusChange(ActivityStatus.IN_PROGRESS, reason);

		ValidationResult result = ActivityValidator.validateForStart(this, reason);
		if (!result.isValid()) {
			throw new ActivityStatusException(result.getErrorsAsString());
		}

		validateStartDate();
		updateStatus(ActivityStatus.IN_PROGRESS, reason);
		updateActualStartDate();
	}

	private void validateStartDate() {
		if (schedule != null && schedule.plannedStartDate() != null) {
			if (schedule.plannedStartDate().isBefore(LocalDateTime.now().minusDays(1))) {
				throw new ActivityScheduleException("Cannot start activity more than 1 day after planned start date");
			}
		}
	}

	private void updateActualStartDate() {
		if (schedule != null && schedule.actualStartDate() == null) {
			this.schedule = new ActivitySchedule(schedule.plannedStartDate(), schedule.plannedEndDate(), LocalDateTime.now(), schedule.actualEndDate());
		}
	}

	public void complete(String reason) {
		validateStatusChange(ActivityStatus.COMPLETED, reason);

		ValidationResult result = ActivityValidator.validateForCompletion(this, reason);
		if (!result.isValid()) {
			throw new ActivityStatusException(result.getErrorsAsString());
		}

		updateStatus(ActivityStatus.COMPLETED, reason);
		updateActualEndDate();
		validateActualCost();
		validateSuccessCriteria();
	}

	private void updateActualEndDate() {
		if (schedule != null && schedule.actualEndDate() == null) {
			this.schedule = new ActivitySchedule(schedule.plannedStartDate(), schedule.plannedEndDate(), schedule.actualStartDate(), LocalDateTime.now());
		}
	}

	private void validateActualCost() {
		if (cost != null && cost.actualCost() == null) {
			System.out.println("Warning: Activity completed without recording actual cost");
		}
	}

	private void validateSuccessCriteria() {
		if (successCriteria != null && !successCriteria.trim().isEmpty()) {
			System.out.println("Success criteria defined: " + successCriteria);
		}
	}

	public void cancel(String reason) {
		validateStatusChange(ActivityStatus.CANCELLED, reason);

		ValidationResult result = ActivityValidator.validateForCancellation(this, reason);
		if (!result.isValid()) {
			throw new ActivityStatusException(result.getErrorsAsString());
		}

		validateInProgressCancellation(reason);
		checkDependenciesBeforeCancellation();
		updateStatus(ActivityStatus.CANCELLED, reason);
		updateCancellationEndDate();
	}

	private void validateInProgressCancellation(String reason) {
		if (status == ActivityStatus.IN_PROGRESS) {
			if (reason == null || reason.trim().length() < 20) {
				throw new ActivityBusinessRuleException("Detailed justification (min 20 chars) required for cancelling in-progress activity");
			}
		}
	}

	private void checkDependenciesBeforeCancellation() {
		if (dependencies != null) {
			System.out.println("Warning: Activity has dependencies. Cancelling may affect other activities.");
		}
	}

	private void updateCancellationEndDate() {
		if (schedule != null && schedule.actualEndDate() == null) {
			this.schedule = new ActivitySchedule(schedule.plannedStartDate(), schedule.plannedEndDate(), schedule.actualStartDate(), LocalDateTime.now());
		}
	}

	public void block(String reason) {
		validateStatusChange(ActivityStatus.BLOCKED, reason);

		ValidationResult result = ActivityValidator.validateForBlock(this, reason);
		if (!result.isValid()) {
			throw new ActivityStatusException(result.getErrorsAsString());
		}

		validateBlockStatus();
		updateStatus(ActivityStatus.BLOCKED, reason);
		notifyUserAboutBlock(reason);
	}

	private void validateBlockStatus() {
		if (status == ActivityStatus.COMPLETED || status == ActivityStatus.CANCELLED) {
			throw new ActivityStatusException("Cannot block completed or cancelled activity");
		}

		if (status != ActivityStatus.IN_PROGRESS && status != ActivityStatus.PLANNED) {
			throw new ActivityStatusException("Can only block planned or in-progress activities");
		}
	}

	private void notifyUserAboutBlock(String reason) {
		if (assignedToUserId != null) {
			log.info("Notification: Activity '{}' has been blocked. Reason: {}. User ID: {}", name, reason, assignedToUserId);
		}
	}

	public void unblock(String reason) {
		if (status != ActivityStatus.BLOCKED) {
			throw new ActivityStatusException("Can only unblock blocked activities");
		}

		ActivityStatus previousStatus = getPreviousStatus();
		validateStatusChange(previousStatus, reason);
		updateStatus(previousStatus, reason);
	}

	private ActivityStatus getPreviousStatus() {
		// Placeholder logic; in a real system, this would track status history
		return ActivityStatus.IN_PROGRESS;
	}

	public void updateActualCost(BigDecimal actualCost, String reason) {
		ValidationResult result = ActivityValidator.validateCostUpdate(this, actualCost);
		if (!result.isValid()) {
			throw new ActivityScheduleException(result.getErrorsAsString());
		}

		validateCostExceedsBudget(actualCost);
		updateCostRecord(actualCost, reason);
	}

	private void validateCostExceedsBudget(BigDecimal actualCost) {
		if (cost != null && cost.plannedCost() != null) {
			BigDecimal maxAllowed = cost.plannedCost().multiply(new BigDecimal("1.20"));
			if (actualCost.compareTo(maxAllowed) > 0) {
				throw new ActivityCostException(String.format("Actual cost cannot exceed planned cost by more than 20%% " + "(Planned: %s, Actual: %s, Max allowed: %s)", cost.plannedCost(), actualCost, maxAllowed));
			}

			BigDecimal variance = calculateCostVariance(cost.plannedCost(), actualCost);
			if (variance.abs().compareTo(new BigDecimal("0.10")) > 0) {
				log.warn(String.format("Warning: Significant cost variance detected: %s%%", variance.multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP)));
			}
		}
	}

	private void updateCostRecord(BigDecimal actualCost, String reason) {
		this.cost = new ActivityCost(cost.plannedCost(), actualCost);
		this.updatedAt = LocalDateTime.now();

		if (reason != null) {
			System.out.println("Cost updated. Reason: " + reason);
		}
	}

	private BigDecimal calculateCostVariance(BigDecimal planned, BigDecimal actual) {
		if (planned == null || planned.compareTo(BigDecimal.ZERO) == 0) {
			return BigDecimal.ZERO;
		}
		return actual.subtract(planned).divide(planned, 4, RoundingMode.HALF_UP);
	}

	public void recordActivityDates(LocalDateTime actualStartDate, LocalDateTime actualEndDate) {
		if (schedule == null) {
			throw new ActivityScheduleException("Activity schedule is not set");
		}

		ValidationResult result = ActivityValidator.validateActivityDates(actualStartDate, actualEndDate, schedule);
		if (!result.isValid()) {
			throw new ActivityScheduleException(result.getErrorsAsString());
		}

		this.schedule = new ActivitySchedule(schedule.plannedStartDate(), schedule.plannedEndDate(), actualStartDate, actualEndDate);

		this.updatedAt = LocalDateTime.now();
		calculateTimeVariance(actualStartDate, actualEndDate);
	}

	private void calculateTimeVariance(LocalDateTime actualStartDate, LocalDateTime actualEndDate) {
		if (schedule.plannedStartDate() == null || schedule.plannedEndDate() == null) {
			return;
		}

		long plannedDuration = ChronoUnit.DAYS.between(schedule.plannedStartDate(), schedule.plannedEndDate());
		long actualDuration = ChronoUnit.DAYS.between(actualStartDate, actualEndDate);
		long variance = actualDuration - plannedDuration;

		if (Math.abs(variance) > 7) {
			System.out.println(String.format("Warning: Significant duration variance: %d days (Planned: %d, Actual: %d)", variance, plannedDuration, actualDuration));
		}
	}

	public void updateGeneralInfo(String name, String description, String successCriteria, String targetAudience, Map<String, Object> dependencies) {

		ValidationResult result = ActivityValidator.validateForUpdate(this, null);
		if (!result.isValid()) {
			throw new ActivityValidationException(result.getErrorsAsString());
		}

		boolean hasChanges = applyGeneralInfoUpdates(name, description, successCriteria, targetAudience, dependencies);

		if (hasChanges) {
			this.updatedAt = LocalDateTime.now();
		}
	}

	private boolean applyGeneralInfoUpdates(String name, String description, String successCriteria, String targetAudience, Map<String, Object> dependencies) {

		boolean hasChanges = false;
		hasChanges |= updateName(name);
		hasChanges |= updateDescription(description);
		hasChanges |= updateSuccessCriteria(successCriteria);
		hasChanges |= updateTargetAudience(targetAudience);
		hasChanges |= updateDependencies(dependencies);

		return hasChanges;
	}

	private boolean updateName(String name) {
		if (name == null || name.trim().isEmpty() || name.trim().equals(this.name)) {
			return false;
		}

		validateName(name);
		this.name = name.trim();
		return true;
	}

	private void validateName(String name) {
		if (name.trim().length() < 3) {
			throw new ActivityValidationException("Activity name must be at least 3 characters");
		}

		if (name.trim().length() > 200) {
			throw new ActivityValidationException("Activity name cannot exceed 200 characters");
		}
	}

	private boolean updateDescription(String description) {
		if (description == null || description.trim().equals(this.description)) {
			return false;
		}

		validateDescription(description);
		this.description = description.trim();
		return true;
	}

	private void validateDescription(String description) {
		if (description.length() > 2000) {
			throw new ActivityValidationException("Description cannot exceed 2000 characters");
		}
	}

	private boolean updateSuccessCriteria(String successCriteria) {
		if (successCriteria == null || successCriteria.trim().equals(this.successCriteria)) {
			return false;
		}

		validateSuccessCriteriaLength(successCriteria);
		this.successCriteria = successCriteria.trim();
		return true;
	}

	private void validateSuccessCriteriaLength(String criteria) {
		if (criteria.length() > 1000) {
			throw new ActivityValidationException("Success criteria cannot exceed 1000 characters");
		}
	}

	private boolean updateTargetAudience(String targetAudience) {
		if (targetAudience == null || targetAudience.trim().equals(this.targetAudience)) {
			return false;
		}

		validateTargetAudience(targetAudience);
		this.targetAudience = targetAudience.trim();
		return true;
	}

	private void validateTargetAudience(String audience) {
		if (audience.length() > 500) {
			throw new ActivityValidationException("Target audience cannot exceed 500 characters");
		}
	}

	private boolean updateDependencies(Map<String, Object> dependencies) {
		if (dependencies == null || Objects.equals(dependencies, this.dependencies)) {
			return false;
		}

		validateDependencies(dependencies);
		this.dependencies = dependencies;
		return true;
	}

	private void validateDependencies(Map<String, Object> dependencies) {
		if (dependencies.size() > 50) {
			throw new ActivityValidationException("Too many dependencies (max 50)");
		}
	}

	public void assign(Long userId, String reason) {
		validateUserId(userId);

		if (!Objects.equals(this.assignedToUserId, userId)) {
			this.assignedToUserId = userId;
			this.updatedAt = LocalDateTime.now();
			logAssignment(reason);
			notifyUserIfInProgress();
		}
	}

	private void validateUserId(Long userId) {
		if (userId == null) {
			throw new ActivityValidationException("User ID cannot be null");
		}

		if (userId <= 0) {
			throw new ActivityValidationException("Invalid user ID");
		}
	}

	private void logAssignment(String reason) {
		if (reason != null) {
			System.out.println(String.format("Activity assigned to user %d. Reason: %s", assignedToUserId, reason));
		}
	}

	private void notifyUserIfInProgress() {
		if (status == ActivityStatus.IN_PROGRESS) {
			System.out.println(String.format("Notification: You have been assigned to activity '%s' which is currently in progress", name));
		}
	}

	public void unassign(String reason) {
		if (assignedToUserId == null) {
			return;
		}

		Long previousUser = assignedToUserId;
		assignedToUserId = null;
		updatedAt = LocalDateTime.now();
		logUnassignment(previousUser, reason);
		checkForBlocking();
	}

	private void logUnassignment(Long previousUser, String reason) {
		if (reason != null) {
			System.out.println(String.format("Activity unassigned from user %d. Reason: %s", previousUser, reason));
		}
	}

	private void checkForBlocking() {
		if (status == ActivityStatus.IN_PROGRESS) {
			System.out.println("Warning: In-progress activity has been unassigned. Consider blocking it.");
		}
	}

	private void validateStatusChange(ActivityStatus newStatus, String reason) {
		if (newStatus == null) {
			throw new ActivityValidationException("New status is required");
		}

		if (status == newStatus) {
			throw new ActivityStatusException(String.format("Activity is already in %s status", newStatus));
		}

		if (isSignificantStatusChange(newStatus) && (reason == null || reason.trim().isEmpty())) {
			throw new ActivityValidationException("Reason is required for significant status changes");
		}
	}

	private boolean isSignificantStatusChange(ActivityStatus newStatus) {
		return newStatus == ActivityStatus.CANCELLED || newStatus == ActivityStatus.BLOCKED || newStatus == ActivityStatus.COMPLETED;
	}

	private void updateStatus(ActivityStatus newStatus, String reason) {
		status = newStatus;
		statusChangedAt = LocalDateTime.now();
		lastStatusChangeReason = reason;
		updatedAt = LocalDateTime.now();
	}

	public boolean hasStarted() {
		return schedule != null && schedule.plannedStartDate() != null && LocalDateTime.now().isAfter(schedule.plannedStartDate());
	}

	public boolean isOverdue() {
		if (schedule == null || schedule.plannedEndDate() == null) {
			return false;
		}

		if (status == ActivityStatus.COMPLETED || status == ActivityStatus.CANCELLED) {
			return false;
		}

		return LocalDateTime.now().isAfter(schedule.plannedEndDate());
	}

	public long getDaysOverdue() {
		if (!isOverdue() || schedule.plannedEndDate() == null) {
			return 0;
		}

		return ChronoUnit.DAYS.between(schedule.plannedEndDate(), LocalDateTime.now());
	}

	public BigDecimal getCostVariancePercentage() {
		if (cost == null || cost.plannedCost() == null || cost.actualCost() == null) {
			return null;
		}

		if (cost.plannedCost().compareTo(BigDecimal.ZERO) == 0) {
			return null;
		}

		return cost.actualCost().subtract(cost.plannedCost()).divide(cost.plannedCost(), 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
	}

	public boolean isCritical() {
		return isCriticalActivity();
	}

	public boolean canBeDeleted() {
		return status == ActivityStatus.PLANNED || status == ActivityStatus.CANCELLED;
	}

	public boolean requiresAttention() {
		return isOverdue() || status == ActivityStatus.BLOCKED || (getCostVariancePercentage() != null && getCostVariancePercentage().abs().compareTo(new BigDecimal("20")) > 0);
	}
}