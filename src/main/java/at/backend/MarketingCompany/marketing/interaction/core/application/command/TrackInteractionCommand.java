package at.backend.MarketingCompany.marketing.interaction.core.application.command;

import com.fasterxml.jackson.databind.JsonNode;

import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.MarketingChannelId;
import at.backend.MarketingCompany.marketing.interaction.core.domain.entity.CreateInteractionParams;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.DeviceInfo;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.LocationInfo;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.MarketingInteractionType;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.PageInfo;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.UTMParameters;
import lombok.Builder;

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
    JsonNode properties) {

  public boolean hasUtmParameters() {
    return utmSource != null || utmMedium != null || utmCampaign != null ||
        utmContent != null || utmTerm != null;
  }

  public boolean hasLocationData() {
    return countryCode != null || city != null;
  }

  public boolean hasDeviceData() {
    return deviceType != null || deviceOs != null || browser != null;
  }

  public boolean hasPageData() {
    return landingPageUrl != null || referrerUrl != null;
  }

  public CreateInteractionParams toCreateCommand() {
    var builder = CreateInteractionParams.builder()
        .campaignId(campaignId)
        .customerId(customerId)
        .marketingInteractionType(interactionType)
        .sessionId(sessionId);

    if (hasLocationData()) {
      var locationInfo = LocationInfo.create(countryCode, city);
      builder.locationInfo(locationInfo);
    }

    if (hasPageData()) {
      var pageInfo = PageInfo.create(landingPageUrl, referrerUrl);
      builder.pageInfo(pageInfo);
    }

    if (hasUtmParameters()) {
      var utmParameters = UTMParameters.create(utmSource, utmMedium, utmCampaign, utmContent, utmTerm);
      builder.utmParameters(utmParameters);
    }

    if (hasDeviceData()) {
      var deviceInfo = DeviceInfo.create(deviceType, deviceOs, browser);
      builder.deviceInfo(deviceInfo);
    }

    return builder.build();
  }
}
