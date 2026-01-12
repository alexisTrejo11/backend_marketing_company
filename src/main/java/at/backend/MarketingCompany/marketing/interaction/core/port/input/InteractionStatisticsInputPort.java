package at.backend.MarketingCompany.marketing.interaction.core.port.input;

import java.math.BigDecimal;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.interaction.core.application.dto.InteractionStatistics;

public interface InteractionStatisticsInputPort {
  /**
   * Gets interaction statistics for a campaign
   */
  InteractionStatistics getInteractionStatistics(MarketingCampaignId campaignId);

  /**
   * Gets conversion rate for a campaign
   */
  Double getConversionRate(MarketingCampaignId campaignId);

  /**
   * Gets average conversion value for a campaign
   */
  BigDecimal getAverageConversionValue(MarketingCampaignId campaignId);
}
