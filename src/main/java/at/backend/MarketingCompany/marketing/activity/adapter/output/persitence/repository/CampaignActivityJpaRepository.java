package at.backend.MarketingCompany.marketing.activity.adapter.output.persitence.repository;

import at.backend.MarketingCompany.marketing.activity.adapter.output.persitence.model.CampaignActivityEntity;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CampaignActivityJpaRepository extends JpaRepository<CampaignActivityEntity, Long> {
    
    @Query("SELECT a FROM CampaignActivityEntity a WHERE a.deletedAt IS NULL AND a.id = :id")
    Optional<CampaignActivityEntity> findByIdAndNotDeleted(@Param("id") Long id);
    
    @Query("SELECT a FROM CampaignActivityEntity a WHERE a.deletedAt IS NULL " +
           "AND a.campaign.id = :campaignId")
    Page<CampaignActivityEntity> findByCampaignId(
            @Param("campaignId") Long campaignId,
            Pageable pageable);
    
    @Query("SELECT a FROM CampaignActivityEntity a WHERE a.deletedAt IS NULL " +
           "AND a.campaign.id = :campaignId AND a.status = :status")
    Page<CampaignActivityEntity> findByCampaignIdAndStatus(
            @Param("campaignId") Long campaignId,
            @Param("status") ActivityStatus status,
            Pageable pageable);
    
    @Query("SELECT a FROM CampaignActivityEntity a WHERE a.deletedAt IS NULL " +
           "AND a.assignedToUserId = :userId")
    Page<CampaignActivityEntity> findByAssignedUserId(
            @Param("userId") Long userId,
            Pageable pageable);
    
    @Query("SELECT a FROM CampaignActivityEntity a WHERE a.deletedAt IS NULL " +
           "AND a.plannedStartDate >= :startDate AND a.plannedEndDate <= :endDate")
    Page<CampaignActivityEntity> findByPlannedDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);
    
    @Query("SELECT a FROM CampaignActivityEntity a WHERE a.deletedAt IS NULL " +
           "AND a.status IN ('PLANNED', 'IN_PROGRESS') " +
           "AND a.plannedStartDate <= :date")
    Page<CampaignActivityEntity> findUpcomingActivities(
            @Param("date") LocalDateTime date,
            Pageable pageable);
    
    @Query("SELECT COUNT(a) FROM CampaignActivityEntity a WHERE a.deletedAt IS NULL " +
           "AND a.campaign.id = :campaignId AND a.status = :status")
    long countByCampaignIdAndStatus(
            @Param("campaignId") Long campaignId,
            @Param("status") ActivityStatus status);
    
    @Query("SELECT SUM(a.actualCost) FROM CampaignActivityEntity a WHERE a.deletedAt IS NULL " +
           "AND a.campaign.id = :campaignId")
    BigDecimal calculateTotalActualCostByCampaignId(@Param("campaignId") Long campaignId);
    
    @Query("SELECT AVG(a.costOverrunPercentage) FROM CampaignActivityEntity a WHERE a.deletedAt IS NULL " +
           "AND a.campaign.id = :campaignId AND a.costOverrunPercentage IS NOT NULL")
    BigDecimal calculateAverageCostOverrunByCampaignId(@Param("campaignId") Long campaignId);
}