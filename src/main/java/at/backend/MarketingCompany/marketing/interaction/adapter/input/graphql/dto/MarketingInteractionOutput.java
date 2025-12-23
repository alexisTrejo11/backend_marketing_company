package at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MarketingInteractionOutput(
    Long id,
    Long campaignId,
    String campaignName,
    Long customerId,
    String customerName,
    String interactionType,
    LocalDateTime interactionDate,
    Long channelId,
    String channelName,
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
    String properties,
    LocalDateTime createdAt
) {}
