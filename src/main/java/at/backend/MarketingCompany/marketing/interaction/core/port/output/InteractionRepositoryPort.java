package at.backend.MarketingCompany.marketing.interaction.core.port.output;

import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.MarketingChannelId;
import at.backend.MarketingCompany.marketing.interaction.core.domain.entity.CampaignInteraction;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.CampaignInteractionId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

public interface InteractionRepositoryPort {

	CampaignInteraction save(CampaignInteraction interaction);

	void delete(CampaignInteractionId id);

	Optional<CampaignInteraction> findById(CampaignInteractionId id);

	Page<CampaignInteraction> findAll(Pageable pageable);

	Page<CampaignInteraction> findByCampaignId(MarketingCampaignId campaignId, Pageable pageable);

	Map<String, Long> countByInteractionTypeByCampaignId(MarketingCampaignId campaignId);

	Map<String, Long> countByDeviceTypeByCampaignId(MarketingCampaignId campaignId);

	BigDecimal calculateTotalConversionValueByCampaignId(MarketingCampaignId campaignId);

	Long countConversionsByCampaignId(MarketingCampaignId campaignId);

	Page<CampaignInteraction> findByUtmParameters(String utmSource, String utmMedium, String utmCampaign, Pageable pageable);

	Page<CampaignInteraction> findByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

	Long countByCampaignId(MarketingCampaignId campaignId);

	Long countUniqueCustomersByCampaignId(MarketingCampaignId campaignId);

	Long countUniqueChannelsByCampaignId(MarketingCampaignId campaignId);

	Map<String, Long> findTopCountriesByCampaignId(MarketingCampaignId campaignId, int limit);

	Map<String, Long> findTopCitiesByCampaignId(MarketingCampaignId campaignId, int limit);

	Page<CampaignInteraction> findByCampaignIdAndIsConversion(MarketingCampaignId campaignId, boolean isConversion, Pageable pageable);

	Page<CampaignInteraction> findByChannelId(MarketingChannelId channelId, Pageable pageable);

	Page<CampaignInteraction> findByCustomerId(CustomerCompanyId customerId, Pageable pageable);

	Page<CampaignInteraction> findByFilters(Pageable pageable);
}


