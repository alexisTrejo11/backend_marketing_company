package at.backend.MarketingCompany.crm.deal.repository.persistence.repository;

import at.backend.MarketingCompany.crm.shared.enums.DealStatus;
import at.backend.MarketingCompany.crm.deal.repository.persistence.model.DealEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JpaDealRepository extends JpaRepository<DealEntity, String> {

    List<DealEntity> findByCustomerModelId(String customerId);

    List<DealEntity> findByDealStatus(DealStatus status);

    List<DealEntity> findByDealStatusIn(List<DealStatus> statuses);

    List<DealEntity> findByOpportunityId(String opportunityId);

    List<DealEntity> findByCampaignManagerId(String campaignManagerId);

    @Query("SELECT d FROM DealEntity d WHERE d.startDate BETWEEN :startDate AND :endDate")
    List<DealEntity> findDealsBetweenDates(@Param("startDate") LocalDate startDate,
                                           @Param("endDate") LocalDate endDate);

    @Query("SELECT d FROM DealEntity d WHERE d.customerModel.id = :customerId AND d.dealStatus IN :statuses")
    List<DealEntity> findByCustomerAndStatuses(@Param("customerId") String customerId,
                                               @Param("statuses") List<DealStatus> statuses);

    @Query("SELECT COUNT(d) FROM DealEntity d WHERE d.dealStatus = :status")
    long countByStatus(@Param("status") DealStatus status);

    @Query("SELECT d FROM DealEntity d WHERE d.dealStatus NOT IN (:excludedStatuses)")
    List<DealEntity> findActiveDeals(@Param("excludedStatuses") List<DealStatus> excludedStatuses);

    boolean existsByOpportunityId(String opportunityId);
}