package at.backend.MarketingCompany.marketing.campaign.core.ports.input;

import at.backend.MarketingCompany.marketing.campaign.core.application.command.AddCampaignSpendingCommand;
import at.backend.MarketingCompany.marketing.campaign.core.application.command.CreateCampaignCommand;
import at.backend.MarketingCompany.marketing.campaign.core.application.command.UpdateCampaignCommand;
import at.backend.MarketingCompany.marketing.campaign.core.domain.models.MarketingCampaign;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;

public interface CampaignCommandServicePort {

	/**
	 * Creates a new marketing campaign
	 */
	MarketingCampaign createCampaign(CreateCampaignCommand command);

	/**
	 * Updates campaign general information
	 */
	MarketingCampaign updateCampaign(UpdateCampaignCommand command);

	/**
	 * Soft deletes a campaign (only if not active)
	 */
	void deleteCampaign(MarketingCampaignId campaignId);

	/**
	 * Launches a campaign (transition to ACTIVE status)
	 */
	MarketingCampaign launchCampaign(MarketingCampaignId campaignId);

	/**
	 * Pauses an active campaign
	 */
	MarketingCampaign pauseCampaign(MarketingCampaignId campaignId);

	/**
	 * Resumes a paused campaign
	 */
	MarketingCampaign resumeCampaign(MarketingCampaignId campaignId);

	/**
	 * Completes a campaign
	 */
	MarketingCampaign completeCampaign(MarketingCampaignId campaignId);

	/**
	 * Cancels a campaign
	 */
	MarketingCampaign cancelCampaign(MarketingCampaignId campaignId);

	/**
	 * Adds spending to a campaign budget
	 */
	MarketingCampaign addSpending(AddCampaignSpendingCommand command);

	/**
	 * Updates attributed revenue for ROI calculation
	 */
	MarketingCampaign updateAttributedRevenue(MarketingCampaignId campaignId, java.math.BigDecimal revenue);
}