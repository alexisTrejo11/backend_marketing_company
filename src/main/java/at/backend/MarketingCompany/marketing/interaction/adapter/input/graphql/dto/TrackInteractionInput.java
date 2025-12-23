package at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.dto;

import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.MarketingInteractionType;
import jakarta.validation.constraints.Positive;
import org.jetbrains.annotations.NotNull;

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
) {}
