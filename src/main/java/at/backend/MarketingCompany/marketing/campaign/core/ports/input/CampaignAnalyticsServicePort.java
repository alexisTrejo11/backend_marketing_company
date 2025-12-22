package at.backend.MarketingCompany.marketing.campaign.core.ports.input;

import at.backend.MarketingCompany.marketing.campaign.core.application.CampaignPerformanceSummary;
import at.backend.MarketingCompany.marketing.campaign.core.application.CampaignStatistics;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;

public interface CampaignAnalyticsServicePort {
	/**
	 * Gets campaign performance summary with ROI
	 */
	CampaignPerformanceSummary getCampaignPerformance(MarketingCampaignId campaignId);

	/**
	 * Gets overall campaign statistics
	 */
	CampaignStatistics getCampaignStatistics();

	/**
	 * Checks if campaign name is available
	 */
	boolean isCampaignNameAvailable(String name);
}
