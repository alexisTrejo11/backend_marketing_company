package at.backend.MarketingCompany.marketing.attribution.core.domain.entity;

import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.DealId;
import at.backend.MarketingCompany.marketing.attribution.core.domain.valueobject.AttributionModel;
import at.backend.MarketingCompany.marketing.attribution.core.domain.valueobject.CampaignAttributionId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.exception.MarketingDomainException;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.shared.domain.BaseDomainEntity;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CampaignAttribution extends BaseDomainEntity<CampaignAttributionId> {
  private DealId dealId;
  private MarketingCampaignId campaignId;
  private AttributionModel attributionModel;
  private BigDecimal attributionPercentage;
  private BigDecimal attributedRevenue;
  private List<LocalDateTime> touchTimestamps;
  private int touchCount;
  private BigDecimal firstTouchWeight;
  private BigDecimal lastTouchWeight;
  private BigDecimal linearWeight;

  private CampaignAttribution() {
    this.touchTimestamps = new ArrayList<>();
    this.touchCount = 0;
    this.firstTouchWeight = BigDecimal.ZERO;
    this.lastTouchWeight = BigDecimal.ZERO;
    this.linearWeight = BigDecimal.ZERO;
    this.attributedRevenue = BigDecimal.ZERO;
  }

  public CampaignAttribution(CampaignAttributionId id) {
    super(id);
    this.touchTimestamps = new ArrayList<>();
    this.touchCount = 0;
    this.firstTouchWeight = BigDecimal.ZERO;
    this.lastTouchWeight = BigDecimal.ZERO;
    this.linearWeight = BigDecimal.ZERO;
    this.attributedRevenue = BigDecimal.ZERO;
  }

  public static CampaignAttribution create(
      DealId dealId,
      MarketingCampaignId campaignId,
      AttributionModel attributionModel,
      BigDecimal attributionPercentage,
      BigDecimal attributedRevenue) {
    
    if (dealId == null) {
      throw new MarketingDomainException("Deal ID is required");
    }
    if (campaignId == null) {
      throw new MarketingDomainException("Campaign ID is required");
    }
    if (attributionModel == null) {
      throw new MarketingDomainException("Attribution model is required");
    }
    if (attributionPercentage == null || 
        attributionPercentage.compareTo(BigDecimal.ZERO) < 0 ||
        attributionPercentage.compareTo(BigDecimal.valueOf(100)) > 0) {
      throw new MarketingDomainException("Attribution percentage must be between 0 and 100");
    }
    if (attributedRevenue == null || attributedRevenue.compareTo(BigDecimal.ZERO) < 0) {
      throw new MarketingDomainException("Attributed revenue cannot be negative");
    }

    CampaignAttribution attribution = new CampaignAttribution(CampaignAttributionId.generate());
    attribution.dealId = dealId;
    attribution.campaignId = campaignId;
    attribution.attributionModel = attributionModel;
    attribution.attributionPercentage = attributionPercentage;
    attribution.attributedRevenue = attributedRevenue;

    return attribution;
  }

  public static CampaignAttribution reconstruct(CampaignAttributionReconstructParams params) {
    if (params == null) {
      return null;
    }

    CampaignAttribution attribution = new CampaignAttribution();
    attribution.id = params.id();
    attribution.dealId = params.dealId();
    attribution.campaignId = params.campaignId();
    attribution.attributionModel = params.attributionModel();
    attribution.attributionPercentage = params.attributionPercentage();
    attribution.attributedRevenue = params.attributedRevenue() != null ? params.attributedRevenue() : BigDecimal.ZERO;
    attribution.touchTimestamps = params.touchTimestamps() != null ? params.touchTimestamps() : new ArrayList<>();
    attribution.touchCount = params.touchCount() != null ? params.touchCount() : 0;
    attribution.firstTouchWeight = params.firstTouchWeight() != null ? params.firstTouchWeight() : BigDecimal.ZERO;
    attribution.lastTouchWeight = params.lastTouchWeight() != null ? params.lastTouchWeight() : BigDecimal.ZERO;
    attribution.linearWeight = params.linearWeight() != null ? params.linearWeight() : BigDecimal.ZERO;
    attribution.createdAt = params.createdAt();
    attribution.updatedAt = params.updatedAt();
    attribution.deletedAt = params.deletedAt();
    attribution.version = params.version();

    return attribution;
  }

  public void addTouchpoint(LocalDateTime timestamp) {
    if (timestamp == null) {
      throw new MarketingDomainException("Touchpoint timestamp is required");
    }
    this.touchTimestamps.add(timestamp);
    this.touchCount = touchTimestamps.size();
  }


}