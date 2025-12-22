package at.backend.MarketingCompany.marketing.campaign.core.ports.input;

import at.backend.MarketingCompany.marketing.campaign.core.application.query.CampaignQuery;
import at.backend.MarketingCompany.marketing.campaign.core.domain.models.MarketingCampaign;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.CampaignStatus;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CampaignQueryServicePort {
	/**
	 * Retrieves a campaign by its ID
	 */
	MarketingCampaign getCampaignById(MarketingCampaignId campaignId);

	/**
	 * Retrieves all campaigns with pagination
	 */
	Page<MarketingCampaign> getAllCampaigns(Pageable pageable);

	/**
	 * Searches campaigns by filters
	 */
	Page<MarketingCampaign> searchCampaigns(CampaignQuery query, Pageable pageable);

	/**
	 * Retrieves campaigns by status
	 */
	Page<MarketingCampaign> getCampaignsByStatus(CampaignStatus status, Pageable pageable);

	/**
	 * Retrieves expired active campaigns that should be completed
	 */
	Page<MarketingCampaign> getExpiredActiveCampaigns(Pageable pageable);

	/**
	 * Retrieves campaigns that need optimization (low ROI)
	 */
	Page<MarketingCampaign> getCampaignsNeedingOptimization(Pageable pageable);

	/**
	 * Retrieves high-performing campaigns
	 */
	Page<MarketingCampaign> getHighPerformingCampaigns(Pageable pageable);
}