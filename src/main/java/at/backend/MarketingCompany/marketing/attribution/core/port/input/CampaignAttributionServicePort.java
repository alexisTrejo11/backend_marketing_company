package at.backend.MarketingCompany.marketing.attribution.core.port.input;

import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.DealId;
import at.backend.MarketingCompany.marketing.attribution.core.application.command.*;
import at.backend.MarketingCompany.marketing.attribution.core.application.dto.AttributionStatistics;
import at.backend.MarketingCompany.marketing.attribution.core.application.queries.AttributionQuery;
import at.backend.MarketingCompany.marketing.attribution.core.domain.entity.CampaignAttribution;
import at.backend.MarketingCompany.marketing.attribution.core.domain.valueobject.AttributionModel;
import at.backend.MarketingCompany.marketing.attribution.core.domain.valueobject.CampaignAttributionId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CampaignAttributionServicePort {

  /**
   * Creates a new campaign attribution
   */
  CampaignAttribution createAttribution(CreateAttributionCommand command);
  
  /**
   * Updates an attribution
   */
  CampaignAttribution updateAttribution(UpdateAttributionCommand command);
  
  /**
   * Adds a touchpoint to an attribution
   */
  CampaignAttribution addTouchpoint(AddTouchpointCommand command);
  
  /**
   * Recalculates attribution with a different model
   */
  CampaignAttribution recalculateAttribution(RecalculateAttributionCommand command);
  
  /**
   * Deletes an attribution (soft delete)
   */
  void deleteAttribution(CampaignAttributionId attributionId);
  

  /**
   * Retrieves an attribution by ID
   */
  CampaignAttribution getAttributionById(CampaignAttributionId attributionId);
  
  /**
   * Searches attributions with filters
   */
  Page<CampaignAttribution> searchAttributions(AttributionQuery query, Pageable pageable);
  
  /**
   * Retrieves attributions by deal
   */
  Page<CampaignAttribution> getAttributionsByDeal(DealId dealId, Pageable pageable);
  
  /**
   * Retrieves attributions by campaign
   */
  Page<CampaignAttribution> getAttributionsByCampaign(MarketingCampaignId campaignId, Pageable pageable);
  
  /**
   * Retrieves attributions by model
   */
  Page<CampaignAttribution> getAttributionsByModel(
      AttributionModel model,
      Pageable pageable
  );
  
  /**
   * Retrieves top attributed campaigns
   */
  Page<CampaignAttribution> getTopAttributedCampaigns(Pageable pageable);
  
  // ========== ANALYTICS OPERATIONS ==========
  
  /**
   * Gets attribution statistics for a campaign
   */
  AttributionStatistics getAttributionStatistics(MarketingCampaignId campaignId);
  
  /**
   * Gets total attributed revenue for a campaign
   */
  java.math.BigDecimal getTotalAttributedRevenue(MarketingCampaignId campaignId);
  
  /**
   * Gets average attribution percentage
   */
  java.math.BigDecimal getAverageAttributionPercentage(MarketingCampaignId campaignId);
}