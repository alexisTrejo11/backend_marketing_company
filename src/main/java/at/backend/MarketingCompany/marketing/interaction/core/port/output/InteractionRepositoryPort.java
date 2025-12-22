package at.backend.MarketingCompany.marketing.interaction.core.port.output;


import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.MarketingChannelId;
import at.backend.MarketingCompany.marketing.interaction.core.domain.entity.CampaignInteraction;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.CampaignInteractionId;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.MarketingInteractionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public interface InteractionRepositoryPort {
    
    CampaignInteraction save(CampaignInteraction interaction);
    
    Optional<CampaignInteraction> findById(CampaignInteractionId id);
    
    void delete(CampaignInteractionId id);
    
    Page<CampaignInteraction> findByCampaignId(MarketingCampaignId campaignId, Pageable pageable);
    
    Page<CampaignInteraction> findByCustomerId(CustomerCompanyId customerId, Pageable pageable);
    
    Page<CampaignInteraction> findByCampaignIdAndConversionStatus(
		    MarketingCampaignId campaignId,
            Boolean isConversion,
            Pageable pageable);
    
    Page<CampaignInteraction> findByDateRange(
            LocalDateTime startDate, 
            LocalDateTime endDate, 
            Pageable pageable);
    
    Page<CampaignInteraction> findByCampaignIdAndInteractionType(
		    MarketingCampaignId campaignId,
            MarketingInteractionType interactionType,
            Pageable pageable);
    
    Page<CampaignInteraction> findByChannelId(MarketingChannelId channelId, Pageable pageable);
    
    long countConversionsByCampaignId(MarketingCampaignId campaignId);
    
    BigDecimal calculateTotalConversionValueByCampaignId(MarketingCampaignId campaignId);
    
    long countUniqueCustomersByCampaignId(MarketingCampaignId campaignId);
}