package at.backend.MarketingCompany.marketing.activity.core.domain.entity;

import at.backend.MarketingCompany.marketing.activity.adapter.output.persitence.model.CampaignActivityEntity;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.*;
import at.backend.MarketingCompany.marketing.campaign.core.domain.exception.InvalidCampaignStateException;
import at.backend.MarketingCompany.marketing.campaign.core.domain.exception.MarketingDomainException;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.shared.domain.BaseDomainEntity;
import lombok.Getter;

import java.util.Map;

@Getter
public class CampaignActivity extends BaseDomainEntity<CampaignActivityId> {
  private MarketingCampaignId campaignId;
  private String name;
  private String description;
  private ActivityType activityType;
  private ActivityStatus status;
  private ActivitySchedule schedule;
  private ActivityCost cost;
  private Long assignedToUserId;
  private String deliveryChannel;
  private String successCriteria;
  private String targetAudience;
  private Map<String, Object> dependencies;

  private CampaignActivity() {
    this.status = ActivityStatus.PLANNED;
  }

  public CampaignActivity(CampaignActivityId id) {
    super(id);
    this.status = ActivityStatus.PLANNED;
  }

  public static CampaignActivity create(
      MarketingCampaignId campaignId,
      String name,
      ActivityType activityType,
      ActivitySchedule schedule,
      ActivityCost cost,
      String deliveryChannel) {
    
    if (campaignId == null) {
      throw new MarketingDomainException("Campaign ID is required");
    }
    if (name == null || name.isBlank()) {
      throw new MarketingDomainException("Activity name is required");
    }
    if (activityType == null) {
      throw new MarketingDomainException("Activity type is required");
    }
    if (schedule == null) {
      throw new MarketingDomainException("Activity schedule is required");
    }
    if (cost == null) {
      throw new MarketingDomainException("Activity cost is required");
    }
    if (deliveryChannel == null || deliveryChannel.isBlank()) {
      throw new MarketingDomainException("Delivery channel is required");
    }

    CampaignActivity activity = new CampaignActivity(CampaignActivityId.generate());
    activity.campaignId = campaignId;
    activity.name = name;
    activity.activityType = activityType;
    activity.schedule = schedule;
    activity.cost = cost;
    activity.deliveryChannel = deliveryChannel;
    activity.status = ActivityStatus.PLANNED;

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
    if (status != ActivityStatus.PLANNED) {
      throw new InvalidCampaignStateException("Only planned activities can be started");
    }
    this.status = ActivityStatus.IN_PROGRESS;
  }

  public void complete() {
    if (status != ActivityStatus.IN_PROGRESS) {
      throw new InvalidCampaignStateException("Only in-progress activities can be completed");
    }
    this.status = ActivityStatus.COMPLETED;
  }

  public void cancel() {
    if (status == ActivityStatus.COMPLETED) {
      throw new InvalidCampaignStateException("Cannot cancel completed activity");
    }
    this.status = ActivityStatus.CANCELLED;
  }

  public void block() {
    if (status == ActivityStatus.COMPLETED || status == ActivityStatus.CANCELLED) {
      throw new InvalidCampaignStateException("Cannot block completed or cancelled activity");
    }
    this.status = ActivityStatus.BLOCKED;
  }


}

