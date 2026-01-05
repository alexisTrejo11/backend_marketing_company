package at.backend.MarketingCompany.crm.interaction.adapter.input.graphql.dto.input;

import java.time.LocalDateTime;
import java.util.List;

import at.backend.MarketingCompany.marketing.interaction.core.application.query.InteractionQuery;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.MarketingInteractionType;

public record InteractionFilterInput(
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

  public InteractionQuery toQuery() {
    return InteractionQuery.builder()
        .campaignId(this.campaignId)
        .customerId(this.customerId)
        .channelId(this.channelId)
        .interactionTypes(this.interactionTypes)
        .isConversion(this.isConversion)
        .dateFrom(this.dateFrom)
        .dateTo(this.dateTo)
        .utmSource(this.utmSource)
        .utmMedium(this.utmMedium)
        .utmCampaign(this.utmCampaign)
        .deviceType(this.deviceType)
        .countryCode(this.countryCode)
        .build();
  }
}
