package at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.dto.output;

import java.math.BigDecimal;
import java.util.List;

import at.backend.MarketingCompany.marketing.interaction.core.application.dto.DeviceBreakdown;
import at.backend.MarketingCompany.marketing.interaction.core.application.dto.GeographicBreakdown;
import at.backend.MarketingCompany.marketing.interaction.core.application.dto.InteractionStatistics;

import lombok.Builder;

@Builder
public record InteractionStatisticsOutput(
    Long campaignId,
    String campaignName,
    Long totalInteractions,
    Long totalConversions,
    Double conversionRate,
    BigDecimal totalConversionValue,
    BigDecimal averageConversionValue,
    Long uniqueCustomers,
    Long uniqueChannels,
    TypeBreakdownResponse typeBreakdown,
    DeviceBreakdownResponse deviceBreakdown,
    GeographicBreakdownResponse geographicBreakdown) {

  public record TypeBreakdownResponse(
      Long adClicks,
      Long adViews,
      Long emailOpens,
      Long emailClicks,
      Long landingPageVisits,
      Long formSubmits,
      Long socialEngagements,
      Long webinarRegistrations,
      Long whitepaperDownloads) {

  }

  public record DeviceBreakdownResponse(
      Long desktop,
      Long mobile,
      Long tablet,
      Long unknown) {

  }

  public record GeographicBreakdownResponse(
      List<CountryCount> topCountries,
      List<CityCount> topCities) {

  }

  public record CountryCount(String countryCode, Long count) {

  }

  public record CityCount(String city, Long count) {

  }

  public static InteractionStatisticsOutput from(InteractionStatistics stats) {
    return new InteractionStatisticsOutput(
        stats.campaignId(),
        stats.campaignName(),
        stats.totalInteractions(),
        stats.totalConversions(),
        stats.conversionRate(),
        stats.totalConversionValue(),
        stats.averageConversionValue(),
        stats.uniqueCustomers(),
        stats.uniqueChannels(),
        toTypeBreakdownResponse(stats.typeBreakdown()),
        toDeviceBreakdownResponse(stats.deviceBreakdown()),
        toGeographicBreakdownResponse(stats.geographicBreakdown()));
  }

  private static TypeBreakdownResponse toTypeBreakdownResponse(
      InteractionStatistics.InteractionTypeBreakdown domain) {

    if (domain == null) {
      return null;
    }

    return new TypeBreakdownResponse(
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

  private static GeographicBreakdownResponse toGeographicBreakdownResponse(
      GeographicBreakdown domain) {
    if (domain == null) {
      return null;
    }

    List<CountryCount> countries = domain.topCountries().entrySet().stream()
        .map(e -> new CountryCount(e.getKey(), e.getValue()))
        .toList();

    List<CityCount> cities = domain.topCities().entrySet().stream()
        .map(e -> new CityCount(e.getKey(), e.getValue()))
        .toList();

    return new GeographicBreakdownResponse(countries, cities);
  }

  private static DeviceBreakdownResponse toDeviceBreakdownResponse(
      DeviceBreakdown domain) {

    if (domain == null) {
      return null;
    }

    return new DeviceBreakdownResponse(
        domain.desktop(),
        domain.mobile(),
        domain.tablet(),
        domain.unknown());
  }
}
