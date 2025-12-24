package at.backend.MarketingCompany.marketing.activity.core.port.input;

import at.backend.MarketingCompany.marketing.activity.core.application.dto.ActivityStatistics;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;

import java.math.BigDecimal;

public interface CampaignActivityStatisticsServicePort {

	/**
	 * Gets activity statistics for a campaign
	 */
	ActivityStatistics getActivityStatistics(MarketingCampaignId campaignId);

	/**
	 * Gets completion rate for a campaign
	 */
	Double getCompletionRate(MarketingCampaignId campaignId);

	/**
	 * Gets average cost overrun
	 */
	BigDecimal getAverageCostOverrun(MarketingCampaignId campaignId);

}
