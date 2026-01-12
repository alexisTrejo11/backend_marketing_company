package at.backend.MarketingCompany.marketing.ab_test.core.port.input;

import at.backend.MarketingCompany.marketing.ab_test.core.application.statistics.AbTestStatistics;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;

import java.math.BigDecimal;

public interface AbTestStatisticsServicePort {
	AbTestStatistics getAbTestStatistics(MarketingCampaignId campaignId);

	Double getCompletionRate(MarketingCampaignId campaignId);

	BigDecimal getAverageSignificance(MarketingCampaignId campaignId);
}