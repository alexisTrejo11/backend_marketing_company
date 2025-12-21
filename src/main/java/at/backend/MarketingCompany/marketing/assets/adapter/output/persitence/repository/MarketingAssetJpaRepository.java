package at.backend.MarketingCompany.marketing.assets.adapter.output.persitence.repository;

import at.backend.MarketingCompany.marketing.assets.adapter.output.persitence.model.MarketingAssetEntity;
import at.backend.MarketingCompany.marketing.assets.core.domain.entity.AssetStatus;
import at.backend.MarketingCompany.marketing.assets.core.domain.entity.AssetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MarketingAssetJpaRepository extends JpaRepository<MarketingAssetEntity, Long> {
    
    @Query("SELECT a FROM MarketingAssetEntity a WHERE a.deletedAt IS NULL AND a.id = :id")
    Optional<MarketingAssetEntity> findByIdAndNotDeleted(@Param("id") Long id);
    
    @Query("SELECT a FROM MarketingAssetEntity a WHERE a.deletedAt IS NULL " +
           "AND a.campaign.id = :campaignId")
    Page<MarketingAssetEntity> findByCampaignId(
            @Param("campaignId") Long campaignId,
            Pageable pageable);
    
    @Query("SELECT a FROM MarketingAssetEntity a WHERE a.deletedAt IS NULL " +
           "AND a.campaign.id = :campaignId AND a.assetType = :assetType")
    Page<MarketingAssetEntity> findByCampaignIdAndAssetType(
            @Param("campaignId") Long campaignId,
            @Param("assetType") AssetType assetType,
            Pageable pageable);
    
    @Query("SELECT a FROM MarketingAssetEntity a WHERE a.deletedAt IS NULL " +
           "AND a.status = :status")
    Page<MarketingAssetEntity> findByStatus(
            @Param("status") AssetStatus status,
            Pageable pageable);
    
    @Query("SELECT a FROM MarketingAssetEntity a WHERE a.deletedAt IS NULL " +
           "AND a.isPrimaryAsset = true AND a.campaign.id = :campaignId")
    Page<MarketingAssetEntity> findPrimaryAssetsByCampaignId(
            @Param("campaignId") Long campaignId,
            Pageable pageable);
    
    @Query("SELECT a FROM MarketingAssetEntity a WHERE a.deletedAt IS NULL " +
           "AND LOWER(a.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<MarketingAssetEntity> searchByName(
            @Param("searchTerm") String searchTerm,
            Pageable pageable);
    
    @Query("SELECT COUNT(a) FROM MarketingAssetEntity a WHERE a.deletedAt IS NULL " +
           "AND a.campaign.id = :campaignId")
    long countByCampaignId(@Param("campaignId") Long campaignId);
    
    @Query("SELECT SUM(a.clicksCount) FROM MarketingAssetEntity a WHERE a.deletedAt IS NULL " +
           "AND a.campaign.id = :campaignId")
    long sumClicksByCampaignId(@Param("campaignId") Long campaignId);
}