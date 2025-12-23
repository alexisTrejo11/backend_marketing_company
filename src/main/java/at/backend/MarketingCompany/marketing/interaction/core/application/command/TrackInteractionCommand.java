package at.backend.MarketingCompany.marketing.interaction.core.application.command;

import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.MarketingChannelId;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.MarketingInteractionType;
import lombok.Builder;

import java.util.Map;

@Builder
public record TrackInteractionCommand(
    MarketingCampaignId campaignId,
    CustomerCompanyId customerId,
    MarketingInteractionType interactionType,
    String sessionId,
    MarketingChannelId channelId,
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
    Map<String, Object> properties
) {}