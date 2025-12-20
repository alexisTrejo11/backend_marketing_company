package at.backend.MarketingCompany.marketing.interaction.core.domain.entity;

import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.exception.MarketingDomainException;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingChannelId;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.CampaignInteractionId;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.InteractionType;
import at.backend.MarketingCompany.shared.domain.BaseDomainEntity;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class CampaignInteraction extends BaseDomainEntity<CampaignInteractionId> {
  private MarketingCampaignId campaignId;
  private CustomerCompanyId customerId;
  private InteractionType interactionType;
  private LocalDateTime interactionDate;
  private MarketingChannelId channelId;
  private String utmSource;
  private String utmMedium;
  private String utmCampaign;
  private String utmContent;
  private String utmTerm;
  private String deviceType;
  private String deviceOs;
  private String browser;
  private String countryCode;
  private String city;
  private Long dealId;
  private BigDecimal conversionValue;
  private boolean isConversion;
  private String landingPageUrl;
  private String referrerUrl;
  private String sessionId;
  private Map<String, Object> properties;

  private CampaignInteraction() {
    this.interactionDate = LocalDateTime.now();
    this.isConversion = false;
  }

  public CampaignInteraction(CampaignInteractionId id) {
    super(id);
    this.interactionDate = LocalDateTime.now();
    this.isConversion = false;
  }

  public static CampaignInteraction create(
      MarketingCampaignId campaignId,
      CustomerCompanyId customerId,
      InteractionType interactionType,
      String sessionId) {
    
    if (campaignId == null) {
      throw new MarketingDomainException("Campaign ID is required");
    }
    if (customerId == null) {
      throw new MarketingDomainException("Customer ID is required");
    }
    if (interactionType == null) {
      throw new MarketingDomainException("Interaction type is required");
    }

    CampaignInteraction interaction = new CampaignInteraction(CampaignInteractionId.generate());
    interaction.campaignId = campaignId;
    interaction.customerId = customerId;
    interaction.interactionType = interactionType;
    interaction.sessionId = sessionId;
    interaction.interactionDate = LocalDateTime.now();
    interaction.isConversion = false;

    return interaction;
  }

  public static CampaignInteraction reconstruct(CampaignInteractionReconstructParams params) {
    if (params == null) {
      return null;
    }

    CampaignInteraction interaction = new CampaignInteraction();
    interaction.id = params.id();
    interaction.campaignId = params.campaignId();
    interaction.customerId = params.customerId();
    interaction.interactionType = params.interactionType();
    interaction.interactionDate = params.interactionDate() != null ? params.interactionDate() : LocalDateTime.now();
    interaction.channelId = params.channelId();
    interaction.utmSource = params.utmSource();
    interaction.utmMedium = params.utmMedium();
    interaction.utmCampaign = params.utmCampaign();
    interaction.utmContent = params.utmContent();
    interaction.utmTerm = params.utmTerm();
    interaction.deviceType = params.deviceType();
    interaction.deviceOs = params.deviceOs();
    interaction.browser = params.browser();
    interaction.countryCode = params.countryCode();
    interaction.city = params.city();
    interaction.dealId = params.dealId();
    interaction.conversionValue = params.conversionValue();
    interaction.isConversion = params.isConversion() != null ? params.isConversion() : false;
    interaction.landingPageUrl = params.landingPageUrl();
    interaction.referrerUrl = params.referrerUrl();
    interaction.sessionId = params.sessionId();
    interaction.properties = params.properties();
    interaction.createdAt = params.createdAt();
    interaction.updatedAt = params.updatedAt();
    interaction.deletedAt = params.deletedAt();
    interaction.version = params.version();

    return interaction;
  }

  public void markAsConversion(Long dealId, BigDecimal conversionValue) {
    if (dealId == null) {
      throw new MarketingDomainException("Deal ID is required for conversion");
    }
    if (conversionValue == null || conversionValue.compareTo(BigDecimal.ZERO) <= 0) {
      throw new MarketingDomainException("Conversion value must be positive");
    }
    
    this.isConversion = true;
    this.dealId = dealId;
    this.conversionValue = conversionValue;
  }


}
