package at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.controller;

import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.MarketingInteractionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.Map;

public record CreateCampaignInteractionRequest(
    @NotNull(message = "Campaign ID is required")
    @Positive(message = "Campaign ID must be positive")
    Long campaignId,

    @NotNull(message = "Customer ID is required")
    @Positive(message = "Customer ID must be positive")
    Long customerId,

    @NotNull(message = "Interaction type is required")
    MarketingInteractionType interactionType,

    @Positive(message = "Channel ID must be positive")
    Long channelId,

    @Size(max = 100, message = "UTM source cannot exceed 100 characters")
    String utmSource,

    @Size(max = 100, message = "UTM medium cannot exceed 100 characters")
    String utmMedium,

    @Size(max = 100, message = "UTM campaign cannot exceed 100 characters")
    String utmCampaign,

    @Size(max = 100, message = "UTM content cannot exceed 100 characters")
    String utmContent,

    @Size(max = 100, message = "UTM term cannot exceed 100 characters")
    String utmTerm,

    @Size(max = 50, message = "Device type cannot exceed 50 characters")
    String deviceType,

    @Size(max = 50, message = "Device OS cannot exceed 50 characters")
    String deviceOs,

    @Size(max = 100, message = "Browser cannot exceed 100 characters")
    String browser,

    @Size(min = 2, max = 2, message = "Country code must be 2 characters")
    String countryCode,

    @Size(max = 100, message = "City cannot exceed 100 characters")
    String city,

    @Size(max = 500, message = "Landing page URL cannot exceed 500 characters")
    String landingPageUrl,

    @Size(max = 500, message = "Referrer URL cannot exceed 500 characters")
    String referrerUrl,

    @Size(max = 100, message = "Session ID cannot exceed 100 characters")
    String sessionId,

    Map<String, Object> properties
) {
}
