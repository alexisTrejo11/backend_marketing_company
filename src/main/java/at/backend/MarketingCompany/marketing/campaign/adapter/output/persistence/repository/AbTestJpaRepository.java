package at.backend.MarketingCompany.marketing.campaign.adapter.output.persistence.repository;

import at.backend.MarketingCompany.marketing.campaign.adapter.output.persistence.entity.AbTestEntity;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.TestType;
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
public interface AbTestJpaRepository extends JpaRepository<AbTestEntity, Long> {
    
    @Query("SELECT t FROM AbTestEntity t WHERE t.deletedAt IS NULL AND t.id = :id")
    Optional<AbTestEntity> findByIdAndNotDeleted(@Param("id") Long id);

    @Query("SELECT t FROM AbTestEntity t WHERE t.deletedAt IS NULL " +
           "AND t.campaign.id = :campaignId AND t.isCompleted = true")
    List<AbTestEntity> findCompletedTestsByCampaignId(@Param("campaignId") Long campaignId);

    @Query("SELECT t FROM AbTestEntity t WHERE t.deletedAt IS NULL " +
           "AND t.campaign.id = :campaignId")
    Page<AbTestEntity> findByCampaignId(
            @Param("campaignId") Long campaignId,
            Pageable pageable);
    
    @Query("SELECT t FROM AbTestEntity t WHERE t.deletedAt IS NULL " +
           "AND t.testType = :testType")
    Page<AbTestEntity> findByTestType(
            @Param("testType") TestType testType,
            Pageable pageable);
    
    @Query("SELECT t FROM AbTestEntity t WHERE t.deletedAt IS NULL " +
           "AND t.isCompleted = :isCompleted")
    Page<AbTestEntity> findByCompletionStatus(
            @Param("isCompleted") Boolean isCompleted,
            Pageable pageable);
    
    @Query("SELECT t FROM AbTestEntity t WHERE t.deletedAt IS NULL " +
           "AND t.startDate >= :startDate AND t.endDate <= :endDate")
    Page<AbTestEntity> findByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);
    
    @Query("SELECT t FROM AbTestEntity t WHERE t.deletedAt IS NULL " +
           "AND LOWER(t.testName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<AbTestEntity> searchByName(
            @Param("searchTerm") String searchTerm,
            Pageable pageable);
    
    @Query("SELECT COUNT(t) FROM AbTestEntity t WHERE t.deletedAt IS NULL " +
           "AND t.campaign.id = :campaignId AND t.isCompleted = true")
    long countCompletedTestsByCampaignId(@Param("campaignId") Long campaignId);

    long countByCampaignIdAndDeletedAtIsNull(Long campaignId);


    @Query("SELECT AVG(t.statisticalSignificance) FROM AbTestEntity t WHERE t.deletedAt IS NULL " +
           "AND t.campaign.id = :campaignId AND t.statisticalSignificance IS NOT NULL")
    BigDecimal calculateAverageSignificanceByCampaignId(@Param("campaignId") Long campaignId);
}