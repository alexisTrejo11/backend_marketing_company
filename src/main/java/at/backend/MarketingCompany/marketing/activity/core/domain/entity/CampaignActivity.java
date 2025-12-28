package at.backend.MarketingCompany.marketing.activity.core.domain.entity;

import at.backend.MarketingCompany.marketing.activity.adapter.output.persitence.model.CampaignActivityEntity;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.*;
import at.backend.MarketingCompany.marketing.campaign.core.domain.exception.InvalidCampaignStateException;
import at.backend.MarketingCompany.marketing.campaign.core.domain.exception.MarketingDomainException;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.shared.domain.BaseDomainEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
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

	private CampaignActivity() {
		this.status = ActivityStatus.PLANNED;
	}

	public CampaignActivity(CampaignActivityId id) {
		super(id);
		this.status = ActivityStatus.PLANNED;
	}

	public static CampaignActivity create(CreateActivityParams params) {

		ActivityValidator.validateForCreation(params.schedule(), params.cost(), params.campaignId());

		CampaignActivity activity = new CampaignActivity(CampaignActivityId.generate());
		activity.campaignId = params.campaignId();
		activity.name = params.name();
		activity.activityType = params.activityType();
		activity.schedule = params.schedule();
		activity.cost = params.cost();
		activity.deliveryChannel = params.deliveryChannel();
		activity.status = ActivityStatus.PLANNED;
		activity.dependencies = params.dependencies();
		activity.description = params.description();
		activity.successCriteria = params.successCriteria();
		activity.targetAudience = params.targetAudience();

		return activity;
	}

	public static CampaignActivity reconstruct(CampaignActivityReconstructParams params) {
		if (params == null) {
			return null;
		}

		CampaignActivity activity = new CampaignActivity();
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
		activity.createdAt = params.createdAt();
		activity.updatedAt = params.updatedAt();
		activity.deletedAt = params.deletedAt();
		activity.version = params.version();

		return activity;
	}

	public void start() {
		ActivityValidator.validateForStart(this);

		this.status = ActivityStatus.IN_PROGRESS;
	}

	public void complete() {
		ActivityValidator.validateForCompletion(this);

		this.status = ActivityStatus.COMPLETED;
	}

	public void cancel() {
		ActivityValidator.validateForCancellation(this);
		this.status = ActivityStatus.CANCELLED;
	}

	public void block() {
		if (status == ActivityStatus.COMPLETED || status == ActivityStatus.CANCELLED) {
			throw new InvalidCampaignStateException("Cannot block completed or cancelled activity");
		}
		this.status = ActivityStatus.BLOCKED;
	}

	public void updateActualCost(BigDecimal actualCost) {
		ActivityValidator.validateCostUpdate(this, actualCost);
		ActivityCost currentCost = this.cost;

		this.cost = new ActivityCost(currentCost.plannedCost(), actualCost);
	}

	public void recordActivityDates(LocalDateTime actualStartDate, LocalDateTime actualEndDate) {
		if (schedule == null) {
			throw new MarketingDomainException("Activity schedule is not set");
		}

		this.schedule = new ActivitySchedule(
				schedule.plannedStartDate(),
				schedule.plannedEndDate(),
				actualStartDate,
				actualEndDate
		);
	}

	public void updateGeneralInfo(
			String name,
			String description,
			String successCriteria,
			String targetAudience,
			Map<String, Object> dependencies
	) {
		ActivityValidator.validateForUpdate(this);

		if (name != null) this.name = name;
		if (description != null) this.description = description;
		if (successCriteria != null) this.successCriteria = successCriteria;
		if (targetAudience != null) this.targetAudience = targetAudience;
		if (dependencies != null) this.dependencies = dependencies;
	}

	public void assign(Long userId) {
		this.assignedToUserId = userId;
	}

	public void unassign() {
		this.assignedToUserId = null;

	}
}

