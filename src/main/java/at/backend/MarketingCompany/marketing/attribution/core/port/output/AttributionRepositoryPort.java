package at.backend.MarketingCompany.marketing.attribution.core.port.output;


import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.DealId;
import at.backend.MarketingCompany.marketing.attribution.core.domain.entity.CampaignAttribution;
import at.backend.MarketingCompany.marketing.attribution.core.domain.valueobject.AttributionModel;
import at.backend.MarketingCompany.marketing.attribution.core.domain.valueobject.CampaignAttributionId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;

public interface AttributionRepositoryPort {
    CampaignAttribution save(CampaignAttribution attribution);
    
    Optional<CampaignAttribution> findById(CampaignAttributionId id);
    
    void delete(CampaignAttributionId id);
    
    Page<CampaignAttribution> findByDealId(DealId dealId, Pageable pageable);
    
    Page<CampaignAttribution> findByCampaignId(MarketingCampaignId campaignId, Pageable pageable);
    
    Optional<CampaignAttribution> findByDealIdAndCampaignId(DealId dealId, MarketingCampaignId campaignId);
    
    Page<CampaignAttribution> findByAttributionModel(
            AttributionModel attributionModel,
            Pageable pageable);
    
    BigDecimal calculateTotalAttributedRevenueByCampaignId(MarketingCampaignId campaignId);
    
    BigDecimal calculateAverageAttributionPercentageByCampaignId(MarketingCampaignId campaignId);
    
    long countUniqueDealsByCampaignId(MarketingCampaignId campaignId);
    
    boolean existsByDealIdAndCampaignIdAndNotDeleted(DealId dealId, MarketingCampaignId campaignId);
}