package at.backend.MarketingCompany.marketing.metric.adapter.output.persitence.repository;

import at.backend.MarketingCompany.marketing.metric.adapter.output.persitence.model.CampaignMetricEntity;
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
public interface CampaignMetricJpaRepository extends JpaRepository<CampaignMetricEntity, Long> {
    
    @Query("SELECT m FROM CampaignMetricEntity m WHERE m.deletedAt IS NULL AND m.id = :id")
    Optional<CampaignMetricEntity> findByIdAndNotDeleted(@Param("id") Long id);
    
    @Query("SELECT m FROM CampaignMetricEntity m WHERE m.deletedAt IS NULL " +
           "AND m.campaign.id = :campaignId")
    Page<CampaignMetricEntity> findByCampaignId(
            @Param("campaignId") Long campaignId,
            Pageable pageable);
    
    @Query("SELECT m FROM CampaignMetricEntity m WHERE m.deletedAt IS NULL " +
           "AND m.campaign.id = :campaignId AND m.metricType = :metricType")
    Page<CampaignMetricEntity> findByCampaignIdAndMetricType(
            @Param("campaignId") Long campaignId,
            @Param("metricType") CampaignMetricEntity.MetricType metricType,
            Pageable pageable);
    
    @Query("SELECT m FROM CampaignMetricEntity m WHERE m.deletedAt IS NULL " +
           "AND m.isAutomated = :isAutomated")
    Page<CampaignMetricEntity> findByAutomationStatus(
            @Param("isAutomated") Boolean isAutomated,
            Pageable pageable);
    
    @Query("SELECT m FROM CampaignMetricEntity m WHERE m.deletedAt IS NULL " +
           "AND m.lastCalculatedDate >= :fromDate")
    Page<CampaignMetricEntity> findRecentlyUpdated(
            @Param("fromDate") LocalDateTime fromDate,
            Pageable pageable);
    
    @Query("SELECT m FROM CampaignMetricEntity m WHERE m.deletedAt IS NULL " +
           "AND m.isTargetAchieved = :isAchieved AND m.campaign.id = :campaignId")
    Page<CampaignMetricEntity> findByCampaignIdAndTargetAchievement(
            @Param("campaignId") Long campaignId,
            @Param("isAchieved") Boolean isAchieved,
            Pageable pageable);
    
    @Query("SELECT COUNT(m) FROM CampaignMetricEntity m WHERE m.deletedAt IS NULL " +
           "AND m.campaign.id = :campaignId AND m.isTargetAchieved = true")
    long countAchievedMetricsByCampaignId(@Param("campaignId") Long campaignId);
    
    @Query("SELECT AVG(m.currentValue) FROM CampaignMetricEntity m WHERE m.deletedAt IS NULL " +
           "AND m.campaign.id = :campaignId AND m.metricType = :metricType")
    BigDecimal calculateAverageValueByCampaignAndMetricType(
            @Param("campaignId") Long campaignId,
            @Param("metricType") CampaignMetricEntity.MetricType metricType);
}