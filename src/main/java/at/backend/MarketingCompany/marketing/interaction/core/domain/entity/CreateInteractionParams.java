package at.backend.MarketingCompany.marketing.interaction.core.domain.entity;

import at.backend.MarketingCompany.crm.interaction.core.domain.exceptions.InteractionValidationException;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.DeviceInfo;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.LocationInfo;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.MarketingInteractionType;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.PageInfo;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.UTMParameters;
import lombok.Builder;

@Builder
public record CreateInteractionParams(
    MarketingCampaignId campaignId,
    CustomerCompanyId customerId,
    MarketingInteractionType marketingInteractionType,
    String sessionId,
    LocationInfo locationInfo,
    PageInfo pageInfo,
    UTMParameters utmParameters,
    DeviceInfo deviceInfo) {

  private static final int MAX_SESSION_ID_LENGTH = 100;

  public CreateInteractionParams {
    if (campaignId == null) {
      throw new InteractionValidationException("Campaign ID is required");
    }
    if (customerId == null) {
      throw new InteractionValidationException("Customer ID is required");
    }
    if (marketingInteractionType == null) {
      throw new InteractionValidationException("Interaction type is required");
    }
    validateSessionId(sessionId);
  }

  private static void validateSessionId(String sessionId) {
    if (sessionId == null || sessionId.isBlank()) {
      throw new InteractionValidationException("Session ID is required");
    }
    if (sessionId.length() > MAX_SESSION_ID_LENGTH) {
      throw new InteractionValidationException(
          String.format("Session ID cannot exceed %d characters", MAX_SESSION_ID_LENGTH));
    }
  }
}
