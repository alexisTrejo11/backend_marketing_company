package at.backend.MarketingCompany.marketing.interaction.core.port.input;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.MarketingChannelId;
import at.backend.MarketingCompany.marketing.interaction.core.application.query.InteractionQuery;
import at.backend.MarketingCompany.marketing.interaction.core.domain.entity.CampaignInteraction;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.CampaignInteractionId;

public interface InteractionQueryInputPort {
  /**
   * Retrieves an interaction by ID
   */
  CampaignInteraction getInteractionById(CampaignInteractionId interactionId);

  /**
   * Searches interactions with filters
   */
  Page<CampaignInteraction> searchInteractions(InteractionQuery query, Pageable pageable);

  /**
   * Retrieves interactions by campaign
   */
  Page<CampaignInteraction> getInteractionsByCampaign(MarketingCampaignId campaignId, Pageable pageable);

  /**
   * Retrieves interactions by customer
   */
  Page<CampaignInteraction> getInteractionsByCustomer(CustomerCompanyId customerId, Pageable pageable);

  /**
   * Retrieves conversions by campaign
   */
  Page<CampaignInteraction> getConversionsByCampaign(MarketingCampaignId campaignId, Pageable pageable);

  /**
   * Retrieves interactions by channel
   */
  Page<CampaignInteraction> getInteractionsByChannel(MarketingChannelId channelId, Pageable pageable);

  /**
   * Retrieves interactions by date range
   */
  Page<CampaignInteraction> getInteractionsByDateRange(LocalDateTime startDate, LocalDateTime endDate,
      Pageable pageable);

  /**
   * Retrieves interactions by UTM parameters
   */
  Page<CampaignInteraction> getInteractionsByUtm(String utmSource, String utmMedium, String utmCampaign,
      Pageable pageable);
}
