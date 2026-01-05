package at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.dto.output;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Builder;

@Builder
public record MarketingInteractionOutput(
    Long id,
    Long campaignId,
    Long customerId,
    String interactionType,
    LocalDateTime interactionDate,
    Long channelId,
    UTMParametersOutput utmParameters,
    DeviceInfoOutput deviceInfo,
    LocationInfoOutput locationInfo,
    PageInfoOutput pageInfo,
    Long dealId,
    BigDecimal conversionValue,
    Boolean isConversion,
    LocalDateTime conversionDate,
    String conversionNotes,
    String sessionId,
    JsonNode properties,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {

  @Builder
  public record UTMParametersOutput(
      String source,
      String medium,
      String campaign,
      String content,
      String term) {
  }

  @Builder
  public record DeviceInfoOutput(
      String type,
      String os,
      String browser) {
  }

  @Builder
  public record LocationInfoOutput(
      String countryCode,
      String city) {
  }

  @Builder
  public record PageInfoOutput(
      String landingPageUrl,
      String referrerUrl) {
  }
}
