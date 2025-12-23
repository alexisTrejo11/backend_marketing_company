package at.backend.MarketingCompany.marketing.interaction.core.port.input;

import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.MarketingChannelId;
import at.backend.MarketingCompany.marketing.interaction.core.application.command.MarkAsConversionCommand;
import at.backend.MarketingCompany.marketing.interaction.core.application.command.TrackInteractionCommand;
import at.backend.MarketingCompany.marketing.interaction.core.application.query.InteractionQuery;
import at.backend.MarketingCompany.marketing.interaction.core.application.dto.InteractionStatistics;
import at.backend.MarketingCompany.marketing.interaction.core.domain.entity.CampaignInteraction;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.CampaignInteractionId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface CampaignInteractionServicePort {

	/**
	 * Tracks a new campaign interaction
	 */
	CampaignInteraction trackInteraction(TrackInteractionCommand command);

	/**
	 * Marks an interaction as a conversion
	 */
	CampaignInteraction markAsConversion(MarkAsConversionCommand command);

	/**
	 * Deletes an interaction (soft delete)
	 */
	void deleteInteraction(CampaignInteractionId interactionId);


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
	Page<CampaignInteraction> getInteractionsByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

	/**
	 * Retrieves interactions by UTM parameters
	 */
	Page<CampaignInteraction> getInteractionsByUtm(String utmSource, String utmMedium, String utmCampaign, Pageable pageable);


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