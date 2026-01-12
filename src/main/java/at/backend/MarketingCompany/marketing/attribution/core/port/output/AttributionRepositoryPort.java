package at.backend.MarketingCompany.marketing.attribution.core.port.output;


import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.DealId;
import at.backend.MarketingCompany.marketing.attribution.core.domain.entity.CampaignAttribution;
import at.backend.MarketingCompany.marketing.attribution.core.domain.valueobject.AttributionModel;
import at.backend.MarketingCompany.marketing.attribution.core.domain.valueobject.CampaignAttributionId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AttributionRepositoryPort {
	CampaignAttribution save(CampaignAttribution attribution);

	Optional<CampaignAttribution> findById(CampaignAttributionId id);

	void delete(CampaignAttributionId id);

	Page<CampaignAttribution> findTopAttributedCampaigns(Pageable pageable);

	Page<CampaignAttribution> findAll(Pageable pageable);

	Page<CampaignAttribution> findByFilters(
			DealId dealId,
			MarketingCampaignId campaignId,
			List<AttributionModel> attributionModel,
			BigDecimal minAttributionPercentage,
			BigDecimal maxAttributionPercentage,
			BigDecimal minAttributedRevenue,
			BigDecimal maxAttributedRevenue,
			Pageable pageable);

	Page<CampaignAttribution> findByDealId(DealId dealId, Pageable pageable);

	Page<CampaignAttribution> findByCampaignId(MarketingCampaignId campaignId, Pageable pageable);

	Optional<CampaignAttribution> findByDealIdAndCampaignId(DealId dealId, MarketingCampaignId campaignId);

	Page<CampaignAttribution> findByAttributionModel(
			AttributionModel attributionModel,
			Pageable pageable);

	BigDecimal calculateTotalAttributedRevenueByCampaignId(MarketingCampaignId campaignId);

	BigDecimal calculateAverageAttributionPercentageByCampaignId(MarketingCampaignId campaignId);

	long countUniqueDealsByCampaignId(MarketingCampaignId campaignId);

	Long countByCampaignId(MarketingCampaignId campaignId);

	Long calculateTotalTouchpointsByCampaignId(MarketingCampaignId campaignId);

	Map<Integer, Long> getTouchpointDistributionByCampaignId(MarketingCampaignId campaignId);

	Map<String, Long> countByAttributionModelByCampaignId(MarketingCampaignId campaignId);

	List<BigDecimal> getAllAttributedRevenuesByCampaignId(MarketingCampaignId campaignId);

	Map<String, BigDecimal> calculateRevenueByModelByCampaignId(MarketingCampaignId campaignId);

	boolean existsByDealIdAndCampaignId(DealId dealId, MarketingCampaignId campaignId);
}