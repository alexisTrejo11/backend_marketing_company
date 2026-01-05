package at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.dto.input;

import org.jetbrains.annotations.NotNull;

import com.fasterxml.jackson.databind.JsonNode;

import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.MarketingChannelId;
import at.backend.MarketingCompany.marketing.interaction.core.application.command.TrackInteractionCommand;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.MarketingInteractionType;
import jakarta.validation.constraints.Positive;

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
    JsonNode properties) {

  public TrackInteractionCommand toCommand() {
    return TrackInteractionCommand.builder()
        .campaignId(new MarketingCampaignId(campaignId))
        .customerId(new CustomerCompanyId(customerId))
        .interactionType(interactionType)
        .sessionId(sessionId)
        .channelId(channelId != null ? new MarketingChannelId(channelId) : null)
        .utmSource(utmSource)
        .utmMedium(utmMedium)
        .utmCampaign(utmCampaign)
        .utmContent(utmContent)
        .utmTerm(utmTerm)
        .deviceType(deviceType)
        .deviceOs(deviceOs)
        .browser(browser)
        .countryCode(countryCode)
        .city(city)
        .landingPageUrl(landingPageUrl)
        .referrerUrl(referrerUrl)
        .properties(properties)
        .build();
  }
}
