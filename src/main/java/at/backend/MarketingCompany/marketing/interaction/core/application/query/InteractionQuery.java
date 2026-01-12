package at.backend.MarketingCompany.marketing.interaction.core.application.query;

import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.MarketingInteractionType;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;

@Builder
public record InteractionQuery(
    Long campaignId,
    Long customerId,
    Long channelId,
    List<MarketingInteractionType> interactionTypes,
    Boolean isConversion,
    LocalDateTime dateFrom,
    LocalDateTime dateTo,
    String utmSource,
    String utmMedium,
    String utmCampaign,
    String deviceType,
    String countryCode) {
  public static InteractionQuery empty() {
    return new InteractionQuery(
        null, null, null, null, null, null, null,
        null, null, null, null, null);
  }

  public boolean isEmpty() {
    return campaignId == null && customerId == null && channelId == null &&
        (interactionTypes == null || interactionTypes.isEmpty()) &&
        isConversion == null && dateFrom == null && dateTo == null &&
        (utmSource == null || utmSource.isBlank()) &&
        (utmMedium == null || utmMedium.isBlank()) &&
        (utmCampaign == null || utmCampaign.isBlank()) &&
        (deviceType == null || deviceType.isBlank()) &&
        (countryCode == null || countryCode.isBlank());
  }
}
