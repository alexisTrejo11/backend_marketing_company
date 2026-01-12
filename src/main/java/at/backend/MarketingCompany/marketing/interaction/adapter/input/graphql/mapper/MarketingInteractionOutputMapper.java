package at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.dto.output.InteractionStatisticsOutput;
import at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.dto.output.MarketingInteractionOutput;
import at.backend.MarketingCompany.marketing.interaction.core.application.dto.DeviceBreakdown;
import at.backend.MarketingCompany.marketing.interaction.core.application.dto.GeographicBreakdown;
import at.backend.MarketingCompany.marketing.interaction.core.application.dto.InteractionStatistics;
import at.backend.MarketingCompany.marketing.interaction.core.domain.entity.CampaignInteraction;
import at.backend.MarketingCompany.shared.PageResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MarketingInteractionOutputMapper {

  public MarketingInteractionOutput toResponse(CampaignInteraction interaction) {
    return MarketingInteractionOutput.builder()
        .id(interaction.getId().getValue())
        .campaignId(interaction.getCampaignId().getValue())
        .customerId(interaction.getCustomerId().getValue())
        .interactionType(interaction.getMarketingInteractionType().name())
        .interactionDate(interaction.getInteractionDate())
        .channelId(interaction.getChannelId() != null ? interaction.getChannelId().getValue() : null)
        .utmParameters(toUtmParametersOutput(interaction))
        .deviceInfo(toDeviceInfoOutput(interaction))
        .locationInfo(toLocationInfoOutput(interaction))
        .pageInfo(toPageInfoOutput(interaction))
        .dealId(interaction.getDealId() != null ? interaction.getDealId().getValue() : null)
        .conversionValue(interaction.getConversionValue())
        .isConversion(interaction.isConversion())
        .conversionDate(interaction.getConversionDate())
        .conversionNotes(interaction.getConversionNotes())
        .sessionId(interaction.getSessionId())
        .properties(interaction.getProperties())
        .createdAt(interaction.getCreatedAt())
        .updatedAt(interaction.getUpdatedAt())
        .build();
  }

  private MarketingInteractionOutput.UTMParametersOutput toUtmParametersOutput(CampaignInteraction interaction) {
    if (interaction.getUtmParameters() == null) {
      return null;
    }

    return MarketingInteractionOutput.UTMParametersOutput.builder()
        .source(interaction.getUtmParameters().getSource())
        .medium(interaction.getUtmParameters().getMedium())
        .campaign(interaction.getUtmParameters().getCampaign())
        .content(interaction.getUtmParameters().getContent())
        .term(interaction.getUtmParameters().getTerm())
        .build();
  }

  private MarketingInteractionOutput.DeviceInfoOutput toDeviceInfoOutput(CampaignInteraction interaction) {
    if (interaction.getDeviceInfo() == null) {
      return null;
    }

    return MarketingInteractionOutput.DeviceInfoOutput.builder()
        .type(interaction.getDeviceInfo().getType())
        .os(interaction.getDeviceInfo().getOs())
        .browser(interaction.getDeviceInfo().getBrowser())
        .build();
  }

  private MarketingInteractionOutput.LocationInfoOutput toLocationInfoOutput(CampaignInteraction interaction) {
    if (interaction.getLocationInfo() == null) {
      return null;
    }

    return MarketingInteractionOutput.LocationInfoOutput.builder()
        .countryCode(interaction.getLocationInfo().getCountryCode())
        .city(interaction.getLocationInfo().getCity())
        .build();
  }

  private MarketingInteractionOutput.PageInfoOutput toPageInfoOutput(CampaignInteraction interaction) {
    if (interaction.getPageInfo() == null) {
      return null;
    }

    return MarketingInteractionOutput.PageInfoOutput.builder()
        .landingPageUrl(interaction.getPageInfo().getLandingPageUrl())
        .referrerUrl(interaction.getPageInfo().getReferrerUrl())
        .build();
  }

  public PageResponse<MarketingInteractionOutput> toPageResponse(Page<CampaignInteraction> page) {
    return PageResponse.of(page.map(this::toResponse));
  }

  public List<MarketingInteractionOutput> toResponseList(List<CampaignInteraction> interactions) {
    if (interactions == null) {
      return List.of();
    }
    return interactions.stream()
        .map(this::toResponse)
        .collect(Collectors.toList());
  }

  public InteractionStatisticsOutput toStatisticsResponse(InteractionStatistics domain) {
    if (domain == null) {
      return null;
    }

    return new InteractionStatisticsOutput(
        domain.campaignId(),
        domain.campaignName(),
        domain.totalInteractions(),
        domain.totalConversions(),
        domain.conversionRate(),
        domain.totalConversionValue(),
        domain.averageConversionValue(),
        domain.uniqueCustomers(),
        domain.uniqueChannels(),
        toTypeBreakdownResponse(domain.typeBreakdown()),
        toDeviceBreakdownResponse(domain.deviceBreakdown()),
        toGeographicBreakdownResponse(domain.geographicBreakdown()));
  }

  private InteractionStatisticsOutput.TypeBreakdownResponse toTypeBreakdownResponse(
      InteractionStatistics.InteractionTypeBreakdown domain) {

    if (domain == null) {
      return null;
    }

    return new InteractionStatisticsOutput.TypeBreakdownResponse(
        domain.adClicks(),
        domain.adViews(),
        domain.emailOpens(),
        domain.emailClicks(),
        domain.landingPageVisits(),
        domain.formSubmits(),
        domain.socialEngagements(),
        domain.webinarRegistrations(),
        domain.whitepaperDownloads());
  }

  private InteractionStatisticsOutput.DeviceBreakdownResponse toDeviceBreakdownResponse(DeviceBreakdown domain) {

    if (domain == null) {
      return null;
    }

    return new InteractionStatisticsOutput.DeviceBreakdownResponse(
        domain.desktop(),
        domain.mobile(),
        domain.tablet(),
        domain.unknown());
  }

  private InteractionStatisticsOutput.GeographicBreakdownResponse toGeographicBreakdownResponse(
      GeographicBreakdown domain) {
    if (domain == null) {
      return null;
    }

    List<InteractionStatisticsOutput.CountryCount> countries = domain.topCountries().entrySet().stream()
        .map(e -> new InteractionStatisticsOutput.CountryCount(e.getKey(), e.getValue()))
        .collect(Collectors.toList());

    List<InteractionStatisticsOutput.CityCount> cities = domain.topCities().entrySet().stream()
        .map(e -> new InteractionStatisticsOutput.CityCount(e.getKey(), e.getValue()))
        .collect(Collectors.toList());

    return new InteractionStatisticsOutput.GeographicBreakdownResponse(countries, cities);
  }

}
