package at.backend.MarketingCompany.marketing.attribution.adapter.output.persitence.repository;

import at.backend.MarketingCompany.marketing.attribution.adapter.output.persitence.model.CampaignAttributionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface CampaignAttributionJpaRepository extends JpaRepository<CampaignAttributionEntity, Long> {
    
    @Query("SELECT a FROM CampaignAttributionEntity a WHERE a.deletedAt IS NULL AND a.id = :id")
    Optional<CampaignAttributionEntity> findByIdAndNotDeleted(@Param("id") Long id);
    
    @Query("SELECT a FROM CampaignAttributionEntity a WHERE a.deletedAt IS NULL " +
           "AND a.deal.id = :dealId")
    Page<CampaignAttributionEntity> findByDealId(
            @Param("dealId") Long dealId,
            Pageable pageable);
    
    @Query("SELECT a FROM CampaignAttributionEntity a WHERE a.deletedAt IS NULL " +
           "AND a.campaign.id = :campaignId")
    Page<CampaignAttributionEntity> findByCampaignId(
            @Param("campaignId") Long campaignId,
            Pageable pageable);
    
    @Query("SELECT a FROM CampaignAttributionEntity a WHERE a.deletedAt IS NULL " +
           "AND a.deal.id = :dealId AND a.campaign.id = :campaignId")
    Optional<CampaignAttributionEntity> findByDealIdAndCampaignId(
            @Param("dealId") Long dealId,
            @Param("campaignId") Long campaignId);
    
    @Query("SELECT a FROM CampaignAttributionEntity a WHERE a.deletedAt IS NULL " +
           "AND a.attributionModel = :attributionModel")
    Page<CampaignAttributionEntity> findByAttributionModel(
            @Param("attributionModel") CampaignAttributionEntity.AttributionModel attributionModel,
            Pageable pageable);
    
    @Query("SELECT SUM(a.attributedRevenue) FROM CampaignAttributionEntity a WHERE a.deletedAt IS NULL " +
           "AND a.campaign.id = :campaignId")
    BigDecimal calculateTotalAttributedRevenueByCampaignId(@Param("campaignId") Long campaignId);
    
    @Query("SELECT AVG(a.attributionPercentage) FROM CampaignAttributionEntity a WHERE a.deletedAt IS NULL " +
           "AND a.campaign.id = :campaignId")
    BigDecimal calculateAverageAttributionPercentageByCampaignId(@Param("campaignId") Long campaignId);
    
    @Query("SELECT COUNT(DISTINCT a.deal.id) FROM CampaignAttributionEntity a WHERE a.deletedAt IS NULL " +
           "AND a.campaign.id = :campaignId")
    long countUniqueDealsByCampaignId(@Param("campaignId") Long campaignId);
}