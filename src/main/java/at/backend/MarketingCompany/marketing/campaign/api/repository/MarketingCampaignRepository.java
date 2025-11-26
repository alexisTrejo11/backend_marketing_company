package at.backend.MarketingCompany.marketing.campaign.api.repository;

import at.backend.MarketingCompany.common.utils.Enums.MarketingCampaign.CampaignStatus;
import at.backend.MarketingCompany.common.utils.Enums.MarketingCampaign.CampaignType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MarketingCampaignRepository extends JpaRepository<MarketingCampaignModel, UUID> {

    List<MarketingCampaignModel> findByStatus(CampaignStatus status);

    List<MarketingCampaignModel> findByType(CampaignType type);

    List<MarketingCampaignModel> findByStartDateBetween(LocalDate startDate, LocalDate endDate);

    /* FIX
     @Query("SELECT mc FROM MarketingCampaign mc JOIN mc.relatedDealEntities d WHERE d.id = :dealId")
    List<MarketingCampaignModel> findByDealId(@Param("dealId") Long dealId);

    @Query("SELECT mc FROM MarketingCampaign mc WHERE mc.status = 'ACTIVE' " +
            "AND EXISTS (SELECT ci FROM CampaignInteractionModel ci WHERE ci.campaign = mc AND ci.customerModel.id = :customerId)")
    List<MarketingCampaignModel> findActiveByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT DISTINCT mc FROM MarketingCampaign mc " +
            "JOIN mc.targetSegments seg " +
            "JOIN seg.customers cust " +
            "WHERE cust.id = :customerId")
    List<MarketingCampaignModel> findByCampaignTargetSegmentCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT mc FROM MarketingCampaign mc WHERE mc.endDate < CURRENT_DATE AND mc.status = 'ACTIVE'")
    List<MarketingCampaignModel> findExpiredActiveCampaigns();

    @Query("SELECT SUM(ca.attributedRevenue) FROM CampaignAttributionModel ca WHERE ca.campaign.id = :campaignId")
    Optional<Double> findTotalAttributedRevenueByCampaignId(@Param("campaignId") Long campaignId);

    @Query("SELECT mc FROM MarketingCampaign mc WHERE " +
            "EXISTS (SELECT ca FROM CampaignActivityModel ca WHERE ca.campaign = mc AND ca.status = 'PLANNED' " +
            "AND ca.plannedStartDate BETWEEN :startDate AND :endDate)")
    List<MarketingCampaignModel> findWithPlannedActivitiesBetweenDates(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query(value = "SELECT c.* FROM marketing_campaigns c " +
            "JOIN campaign_metrics m ON m.campaign_id = c.id " +
            "WHERE m.name = :metricName AND m.value > m.target_value",
            nativeQuery = true)
    List<MarketingCampaignModel> findCampaignsExceedingMetricTarget(@Param("metricName") String metricName);

     */
}