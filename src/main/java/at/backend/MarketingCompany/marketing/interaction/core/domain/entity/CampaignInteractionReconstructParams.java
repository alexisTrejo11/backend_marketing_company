package at.backend.MarketingCompany.marketing.interaction.core.domain.entity;

import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.MarketingChannelId;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.CampaignInteractionId;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.MarketingInteractionType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Builder
public record CampaignInteractionReconstructParams(
    CampaignInteractionId id,
    MarketingCampaignId campaignId,
    CustomerCompanyId customerId,
    MarketingInteractionType marketingInteractionType,
    LocalDateTime interactionDate,
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
    Long dealId,
    BigDecimal conversionValue,
    Boolean isConversion,
    String landingPageUrl,
    String referrerUrl,
    String sessionId,
    Map<String, Object> properties,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime deletedAt,
    Integer version
) {}