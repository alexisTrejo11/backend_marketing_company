package at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.dto;

import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.MarketingChannelId;
import at.backend.MarketingCompany.marketing.interaction.core.application.command.TrackInteractionCommand;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.MarketingInteractionType;
import jakarta.validation.constraints.Positive;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public record TrackInteractionInput(
    @NotNull @Positive Long campaignId,
    @NotNull @Positive Long customerId,
    @NotNull MarketingInteractionType interactionType,
    String sessionId,
    Long channelId,
    String utmSource,
    String utmMedium,
    String utmCampaign,
    String utmContent,
    String utmTerm,
    String deviceType,
    String deviceOs,
    String browser,
    String countryCode,
    String city,
    String landingPageUrl,
    String referrerUrl,
    String properties
) {
	public TrackInteractionCommand toCommand(Map<String, Object> propertiesMap) {
		return new TrackInteractionCommand(
				new MarketingCampaignId(campaignId),
				new CustomerCompanyId(customerId),
				interactionType,
				sessionId,
				channelId != null ? new MarketingChannelId(channelId) : null,
				utmSource,
				utmMedium,
				utmCampaign,
				utmContent,
				utmTerm,
				deviceType,
				deviceOs,
				browser,
				countryCode,
				city,
				landingPageUrl,
				referrerUrl,
				propertiesMap
		);
	}
}
