package at.backend.MarketingCompany.marketing.interaction.adapter.output.persitence.repository;

import at.backend.MarketingCompany.marketing.interaction.adapter.output.persitence.model.CampaignInteractionEntity;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.MarketingInteractionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CampaignInteractionJpaRepository extends JpaRepository<CampaignInteractionEntity, Long> {
    
    @Query("SELECT i FROM CampaignInteractionEntity i WHERE i.deletedAt IS NULL AND i.id = :id")
    Optional<CampaignInteractionEntity> findByIdAndNotDeleted(@Param("id") Long id);
    
    @Query("SELECT i FROM CampaignInteractionEntity i WHERE i.deletedAt IS NULL " +
           "AND i.campaign.id = :campaignId")
    Page<CampaignInteractionEntity> findByCampaignId(
            @Param("campaignId") Long campaignId,
            Pageable pageable);
    
    @Query("SELECT i FROM CampaignInteractionEntity i WHERE i.deletedAt IS NULL " +
           "AND i.customer.id = :customerId")
    Page<CampaignInteractionEntity> findByCustomerId(
            @Param("customerId") Long customerId,
            Pageable pageable);
    
    @Query("SELECT i FROM CampaignInteractionEntity i WHERE i.deletedAt IS NULL " +
           "AND i.campaign.id = :campaignId AND i.isConversion = :isConversion")
    Page<CampaignInteractionEntity> findByCampaignIdAndConversionStatus(
            @Param("campaignId") Long campaignId,
            @Param("isConversion") Boolean isConversion,
            Pageable pageable);
    
    @Query("SELECT i FROM CampaignInteractionEntity i WHERE i.deletedAt IS NULL " +
           "AND i.interactionDate >= :startDate AND i.interactionDate <= :endDate")
    Page<CampaignInteractionEntity> findByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);
    
    @Query("SELECT i FROM CampaignInteractionEntity i WHERE i.deletedAt IS NULL " +
           "AND i.marketingInteractionType = :marketingInteractionType " +
           "AND i.campaign.id = :campaignId")
    Page<CampaignInteractionEntity> findByCampaignIdAndInteractionType(
            @Param("campaignId") Long campaignId,
            @Param("marketingInteractionType") MarketingInteractionType interactionType,
            Pageable pageable);
    
    @Query("SELECT i FROM CampaignInteractionEntity i WHERE i.deletedAt IS NULL " +
           "AND i.channel.id = :channelId")
    Page<CampaignInteractionEntity> findByChannelId(
            @Param("channelId") Long channelId,
            Pageable pageable);
    
    @Query("SELECT COUNT(i) FROM CampaignInteractionEntity i WHERE i.deletedAt IS NULL " +
           "AND i.campaign.id = :campaignId AND i.isConversion = true")
    long countConversionsByCampaignId(@Param("campaignId") Long campaignId);
    
    @Query("SELECT SUM(i.conversionValue) FROM CampaignInteractionEntity i WHERE i.deletedAt IS NULL " +
           "AND i.campaign.id = :campaignId AND i.isConversion = true")
    BigDecimal calculateTotalConversionValueByCampaignId(@Param("campaignId") Long campaignId);
    
    @Query("SELECT COUNT(DISTINCT i.customer.id) FROM CampaignInteractionEntity i WHERE i.deletedAt IS NULL " +
           "AND i.campaign.id = :campaignId")
    long countUniqueCustomersByCampaignId(@Param("campaignId") Long campaignId);

    @Query("SELECT i FROM CampaignInteractionEntity i WHERE i.deletedAt IS NULL")
    Page<CampaignInteractionEntity> findAllNotDeleted(Pageable pageable);

    @Query("SELECT COUNT(i) FROM CampaignInteractionEntity i WHERE i.deletedAt IS NULL " +
           "AND i.campaign.id = :campaignId")
    long countByCampaignId(@Param("campaignId") Long campaignId);

    @Query("SELECT COUNT(DISTINCT i.channel.id) FROM CampaignInteractionEntity i WHERE i.deletedAt IS NULL " +
           "AND i.campaign.id = :campaignId AND i.channel IS NOT NULL")
    long countUniqueChannelsByCampaignId(@Param("campaignId") Long campaignId);

    @Query("SELECT i FROM CampaignInteractionEntity i WHERE i.deletedAt IS NULL " +
           "AND (:utmSource IS NULL OR i.utmSource = :utmSource) " +
           "AND (:utmMedium IS NULL OR i.utmMedium = :utmMedium) " +
           "AND (:utmCampaign IS NULL OR i.utmCampaign = :utmCampaign)")
    Page<CampaignInteractionEntity> findByUtmParameters(
            @Param("utmSource") String utmSource,
            @Param("utmMedium") String utmMedium,
            @Param("utmCampaign") String utmCampaign,
            Pageable pageable);

    @Query("SELECT i.marketingInteractionType as type, COUNT(i) as count " +
           "FROM CampaignInteractionEntity i WHERE i.deletedAt IS NULL " +
           "AND i.campaign.id = :campaignId " +
           "GROUP BY i.marketingInteractionType")
    List<Object[]> countByInteractionTypeByCampaignId(@Param("campaignId") Long campaignId);

    @Query("SELECT i.deviceType as deviceType, COUNT(i) as count " +
           "FROM CampaignInteractionEntity i WHERE i.deletedAt IS NULL " +
           "AND i.campaign.id = :campaignId AND i.deviceType IS NOT NULL " +
           "GROUP BY i.deviceType")
    List<Object[]> countByDeviceTypeByCampaignId(@Param("campaignId") Long campaignId);

    @Query("SELECT i.countryCode as country, COUNT(i) as count " +
           "FROM CampaignInteractionEntity i WHERE i.deletedAt IS NULL " +
           "AND i.campaign.id = :campaignId AND i.countryCode IS NOT NULL " +
           "GROUP BY i.countryCode " +
           "ORDER BY count DESC")
    List<Object[]> findTopCountriesByCampaignId(@Param("campaignId") Long campaignId, Pageable pageable);

    @Query("SELECT i.city as city, COUNT(i) as count " +
           "FROM CampaignInteractionEntity i WHERE i.deletedAt IS NULL " +
           "AND i.campaign.id = :campaignId AND i.city IS NOT NULL " +
           "GROUP BY i.city " +
           "ORDER BY count DESC")
    List<Object[]> findTopCitiesByCampaignId(@Param("campaignId") Long campaignId, Pageable pageable);
}