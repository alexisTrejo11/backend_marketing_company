package at.backend.MarketingCompany.marketing.campaign.core.domain.models;

import at.backend.MarketingCompany.marketing.campaign.core.domain.models.params.MarketingCampaignReconstructParams;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.*;
import at.backend.MarketingCompany.marketing.core.domain.valueobject.*;
import at.backend.MarketingCompany.shared.domain.BaseDomainEntity;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
public class MarketingCampaign extends BaseDomainEntity<MarketingCampaignId> {
  private CampaignName name;
  private String description;
  private CampaignType campaignType;
  private CampaignStatus status;
  private CampaignBudget budget;
  private CampaignPeriod period;
  private Map<String, Object> budgetAllocations;
  private Map<String, Object> targetAudienceDemographics;
  private Map<String, Object> targetLocations;
  private Map<String, Object> targetInterests;
  private String primaryGoal;
  private Map<String, Object> secondaryGoals;
  private Map<String, Object> successMetrics;
  private MarketingChannelId primaryChannelId;
  private Set<CampaignTarget> targets;

  private MarketingCampaign() {
    this.targets = new HashSet<>();
    this.status = CampaignStatus.DRAFT;
  }

  public MarketingCampaign(MarketingCampaignId id) {
    super(id);
    this.targets = new HashSet<>();
    this.status = CampaignStatus.DRAFT;
  }

  public static MarketingCampaign create(
      CampaignName name,
      CampaignType campaignType,
      BigDecimal totalBudget,
      CampaignPeriod period,
      String primaryGoal) {
    
    if (name == null) {
      throw new IllegalArgumentException("Campaign name is required");
    }
    if (campaignType == null) {
      throw new IllegalArgumentException("Campaign type is required");
    }
    if (totalBudget == null || totalBudget.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Total budget must be positive");
    }
    if (period == null) {
      throw new IllegalArgumentException("Campaign period is required");
    }

    MarketingCampaign campaign = new MarketingCampaign(MarketingCampaignId.generate());
    campaign.name = name;
    campaign.campaignType = campaignType;
    campaign.budget = new CampaignBudget(totalBudget, BigDecimal.ZERO);
    campaign.period = period;
    campaign.primaryGoal = primaryGoal;
    campaign.status = CampaignStatus.DRAFT;

    return campaign;
  }

  public static MarketingCampaign reconstruct(MarketingCampaignReconstructParams params) {
    if (params == null) {
      return null;
    }

    MarketingCampaign campaign = new MarketingCampaign();
    campaign.id = params.id();
    campaign.name = params.name();
    campaign.description = params.description();
    campaign.campaignType = params.campaignType();
    campaign.status = params.status() != null ? params.status() : CampaignStatus.DRAFT;
    campaign.budget = params.budget();
    campaign.period = params.period();
    campaign.budgetAllocations = params.budgetAllocations();
    campaign.targetAudienceDemographics = params.targetAudienceDemographics();
    campaign.targetLocations = params.targetLocations();
    campaign.targetInterests = params.targetInterests();
    campaign.primaryGoal = params.primaryGoal();
    campaign.secondaryGoals = params.secondaryGoals();
    campaign.successMetrics = params.successMetrics();
    campaign.primaryChannelId = params.primaryChannelId();
    campaign.targets = params.targets() != null ? params.targets() : new HashSet<>();
    campaign.createdAt = params.createdAt();
    campaign.updatedAt = params.updatedAt();
    campaign.deletedAt = params.deletedAt();
    campaign.version = params.version();

    return campaign;
  }

  public void addTarget(CampaignTarget target) {
    if (target == null) {
      throw new IllegalArgumentException("Target cannot be null");
    }
    this.targets.add(target);
  }

  public void launch() {
    if (status != CampaignStatus.PLANNED && status != CampaignStatus.DRAFT) {
      throw new IllegalStateException("Campaign can only be launched from PLANNED or DRAFT status");
    }
    if (!period.hasStarted()) {
      throw new IllegalStateException("Cannot launch campaign before start date");
    }
    this.status = CampaignStatus.ACTIVE;
  }

  public void pause() {
    if (status != CampaignStatus.ACTIVE) {
      throw new IllegalStateException("Only active campaigns can be paused");
    }
    this.status = CampaignStatus.PAUSED;
  }

  public void resume() {
    if (status != CampaignStatus.PAUSED) {
      throw new IllegalStateException("Only paused campaigns can be resumed");
    }
    this.status = CampaignStatus.ACTIVE;
  }

  public void complete() {
    if (status != CampaignStatus.ACTIVE && status != CampaignStatus.PAUSED) {
      throw new IllegalStateException("Only active or paused campaigns can be completed");
    }
    this.status = CampaignStatus.COMPLETED;
  }

  public void cancel() {
    if (status == CampaignStatus.COMPLETED || status == CampaignStatus.CANCELLED) {
      throw new IllegalStateException("Cannot cancel completed or already cancelled campaign");
    }
    this.status = CampaignStatus.CANCELLED;
  }

  public void addSpending(BigDecimal amount) {
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Spending amount must be positive");
    }
    this.budget = budget.addSpending(amount);
  }

  public boolean isActive() {
    return status == CampaignStatus.ACTIVE && period.isActive();
  }

  public boolean isBudgetNearlyExhausted() {
    return budget.isNearlyExhausted();
  }


}