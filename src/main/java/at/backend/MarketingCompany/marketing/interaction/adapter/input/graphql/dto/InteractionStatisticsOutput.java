package at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

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
    GeographicBreakdownResponse geographicBreakdown
) {
  public record TypeBreakdownResponse(
      Long adClicks,
      Long adViews,
      Long emailOpens,
      Long emailClicks,
      Long landingPageVisits,
      Long formSubmits,
      Long socialEngagements,
      Long webinarRegistrations,
      Long whitepaperDownloads
  ) {}

  public record DeviceBreakdownResponse(
      Long desktop,
      Long mobile,
      Long tablet,
      Long unknown
  ) {}

  public record GeographicBreakdownResponse(
      List<CountryCount> topCountries,
      List<CityCount> topCities
  ) {}

  public record CountryCount(String countryCode, Long count) {}
  public record CityCount(String city, Long count) {}
}