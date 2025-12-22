package at.backend.MarketingCompany.marketing.campaign.core.domain.models;

import at.backend.MarketingCompany.marketing.ab_test.core.domain.valueobject.CampaignType;
import at.backend.MarketingCompany.marketing.campaign.core.domain.exception.MarketingDomainException;
import at.backend.MarketingCompany.marketing.campaign.core.domain.models.params.MarketingCampaignReconstructParams;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.*;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.MarketingChannelId;
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
	private BigDecimal totalAttributedRevenue;


	private MarketingCampaign() {
		this.targets = new HashSet<>();
		this.status = CampaignStatus.DRAFT;
		this.totalAttributedRevenue = BigDecimal.ZERO;

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
			String primaryGoal,
			String description
			) {

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
		campaign.description = description != null ? description : "";
		campaign.campaignType = campaignType;
		campaign.budget = new CampaignBudget(totalBudget, BigDecimal.ZERO);
		campaign.period = period;
		campaign.primaryGoal = primaryGoal;
		campaign.status = CampaignStatus.DRAFT;

		return campaign;
	}

	public void updateGeneralInfo(
			CampaignName name,
			String description,
			CampaignType campaignType,
			CampaignPeriod period,
			String primaryGoal) {

		if (this.status == CampaignStatus.ACTIVE || this.status == CampaignStatus.COMPLETED) {
			throw new IllegalStateException("Cannot update general info of an active or completed campaign");
		}

		this.name = name != null ? name : this.name;
		this.description = description != null ? description : this.description;
		this.campaignType = campaignType != null ? campaignType : this.campaignType;
		this.period = period != null ? period : this.period;
		this.primaryGoal = primaryGoal != null ? primaryGoal : this.primaryGoal;
	}

	public void updateTargetAudience(Map<String, Object> demographics) {
		this.targetAudienceDemographics = demographics;
	}

	public void updateTargetLocations(Map<String, Object> locations) {
		this.targetLocations = locations;
	}

	public void assignPrimaryChannel(MarketingChannelId channelId) {
		this.primaryChannelId = channelId;
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

	public void completeCampaign() {
		if (status != CampaignStatus.ACTIVE && status != CampaignStatus.PAUSED) {
			throw new IllegalStateException("Only active or paused campaigns can be completed");
		}
		this.status = CampaignStatus.COMPLETED;
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


	/**
	 * Calculates and returns the ROI for this campaign
	 * Uses total attributed revenue and spent amount from budget
	 */
	public CampaignROI calculateROI() {
		BigDecimal revenue = totalAttributedRevenue != null ? totalAttributedRevenue : BigDecimal.ZERO;
		BigDecimal cost = budget != null ? budget.spentAmount() : BigDecimal.ZERO;

		return CampaignROI.from(revenue, cost);
	}

	/**
	 * Updates the total attributed revenue
	 * This should be called when campaign attributions are created/updated
	 */
	public void updateAttributedRevenue(BigDecimal revenue) {
		if (revenue == null || revenue.compareTo(BigDecimal.ZERO) < 0) {
			throw new MarketingDomainException("Attributed revenue cannot be negative");
		}
		this.totalAttributedRevenue = revenue;
	}

	/**
	 * Returns the current ROI percentage
	 */
	public BigDecimal getCurrentROIPercentage() {
		return calculateROI().roiPercentage();
	}

	/**
	 * Returns the net profit/loss
	 */
	public BigDecimal getNetProfit() {
		return calculateROI().netProfit();
	}

	/**
	 * Checks if the campaign is profitable
	 */
	public boolean isProfitable() {
		return calculateROI().isProfitable();
	}

	/**
	 * Checks if ROI meets a minimum threshold
	 */
	public boolean meetsROIThreshold(BigDecimal minROI) {
		return calculateROI().meetsThreshold(minROI);
	}

	/**
	 * Calculates how much additional revenue is needed to reach target ROI
	 */
	public BigDecimal getRevenueNeededForTargetROI(BigDecimal targetROI) {
		return calculateROI().revenueNeededForTargetROI(targetROI);
	}

	/**
	 * Returns detailed ROI status
	 */
	public CampaignROI.ROIStatus getROIStatus() {
		return calculateROI().status();
	}

	/**
	 * Checks if the campaign should be optimized based on ROI performance
	 * Returns true if ROI is negative or below break-even
	 */
	public boolean needsOptimization() {
		CampaignROI roi = calculateROI();
		return roi.status() == CampaignROI.ROIStatus.NEGATIVE ||
				roi.status() == CampaignROI.ROIStatus.BREAK_EVEN;
	}

	/**
	 * Checks if the campaign is performing exceptionally well
	 */
	public boolean isHighPerformer() {
		CampaignROI roi = calculateROI();
		return roi.status() == CampaignROI.ROIStatus.EXCELLENT ||
				roi.status() == CampaignROI.ROIStatus.VERY_GOOD;
	}
}