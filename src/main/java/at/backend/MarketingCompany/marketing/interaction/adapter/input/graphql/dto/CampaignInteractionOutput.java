package at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Builder
public record CampaignInteractionOutput(
    Long id,
    Long campaignId,
    Long customerId,
    String interactionType,
    LocalDateTime interactionDate,
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
    Long dealId,
    BigDecimal conversionValue,
    Boolean isConversion,
    String landingPageUrl,
    String referrerUrl,
    String sessionId,
    Map<String, Object> properties,
    LocalDateTime createdAt
) {}