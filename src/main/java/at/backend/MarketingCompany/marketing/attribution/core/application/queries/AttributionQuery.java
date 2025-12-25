package at.backend.MarketingCompany.marketing.attribution.core.application.queries;

import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.DealId;
import at.backend.MarketingCompany.marketing.attribution.core.domain.valueobject.AttributionModel;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;

import java.math.BigDecimal;
import java.util.List;

public record AttributionQuery(
    DealId dealId,
    MarketingCampaignId campaignId,
    List<AttributionModel> attributionModels,
    BigDecimal minAttributionPercentage,
    BigDecimal maxAttributionPercentage,
    BigDecimal minAttributedRevenue,
    BigDecimal maxAttributedRevenue
) {
  public static AttributionQuery empty() {
    return new AttributionQuery(null, null, null, null, null, null, null);
  }

  public boolean isEmpty() {
    return dealId == null &&
           campaignId == null &&
           (attributionModels == null || attributionModels.isEmpty()) &&
           minAttributionPercentage == null &&
           maxAttributionPercentage == null &&
           minAttributedRevenue == null &&
           maxAttributedRevenue == null;
  }
}
