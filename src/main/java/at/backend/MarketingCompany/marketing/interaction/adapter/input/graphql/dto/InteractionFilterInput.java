package at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.dto;

import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.MarketingInteractionType;

import java.util.List;

public record InteractionFilterInput(
    Long campaignId,
    Long customerId,
    Long channelId,
    List<MarketingInteractionType> interactionTypes,
    Boolean isConversion,
    String dateFrom,
    String dateTo,
    String utmSource,
    String utmMedium,
    String utmCampaign,
    String deviceType,
    String countryCode
) {}