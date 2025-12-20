package at.backend.MarketingCompany.marketing.campaign.core.ports.output;

import at.backend.MarketingCompany.marketing.campaign.core.domain.models.MarketingCampaign;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.CampaignStatus;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.CampaignType;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface CampaignRepositoryPort {
    
    MarketingCampaign save(MarketingCampaign campaign);
    
    Optional<MarketingCampaign> findById(MarketingCampaignId id);
    
    void delete(MarketingCampaignId id);
    
    Page<MarketingCampaign> findByFilters(
            CampaignStatus status,
            CampaignType campaignType,
            Long primaryChannelId,
            Pageable pageable);
    
    Page<MarketingCampaign> findByDateRange(
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable);
    
    Page<MarketingCampaign> findByBudgetRange(
            BigDecimal minBudget,
            BigDecimal maxBudget,
            Pageable pageable);
    
    Page<MarketingCampaign> searchByName(String searchTerm, Pageable pageable);
    
    Page<MarketingCampaign> findExpiredActiveCampaigns(Pageable pageable);
    
    long countByStatus(CampaignStatus status);
    
    BigDecimal calculateTotalPlannedBudget();
    
    BigDecimal calculateTotalActiveSpend();
    
    boolean existsByNameAndNotDeleted(String name);
}