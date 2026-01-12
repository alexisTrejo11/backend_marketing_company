package at.backend.MarketingCompany.marketing.interaction.core.application.dto;

import lombok.Builder;
import java.math.BigDecimal;

@Builder
public record InteractionStatistics(
    Long campaignId,
    String campaignName,
    Long totalInteractions,
    Long totalConversions,
    Double conversionRate,
    BigDecimal totalConversionValue,
    BigDecimal averageConversionValue,
    Long uniqueCustomers,
    Long uniqueChannels,
    InteractionTypeBreakdown typeBreakdown,
    DeviceBreakdown deviceBreakdown,
    GeographicBreakdown geographicBreakdown
) {
  @Builder
  public record InteractionTypeBreakdown(
      Long adClicks,
      Long adViews,
      Long emailOpens,
      Long emailClicks,
      Long landingPageVisits,
      Long formSubmits,
      Long socialEngagements,
      Long webinarRegistrations,
      Long whitepaperDownloads
  ) {
  }
}