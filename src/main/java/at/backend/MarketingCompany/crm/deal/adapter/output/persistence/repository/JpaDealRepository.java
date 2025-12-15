package at.backend.MarketingCompany.crm.deal.adapter.output.persistence.repository;

import at.backend.MarketingCompany.crm.deal.adapter.output.persistence.model.DealEntity;
import at.backend.MarketingCompany.crm.shared.enums.DealStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface JpaDealRepository extends JpaRepository<DealEntity, String> {

  @Query("SELECT d FROM DealEntity d WHERE d.customerCompany.id = :customer_id")
  Page<DealEntity> findByCustomerId(@Param("customer_id") String customerId, Pageable pageable);

  List<DealEntity> findByDealStatus(DealStatus status);

  Page<DealEntity> findByDealStatusIn(Set<DealStatus> statuses, Pageable pageable);

  Page<DealEntity> findByOpportunityId(String opportunityId, Pageable pageable);

  Page<DealEntity> findByCampaignManagerId(String campaignManagerId, Pageable pageable);

  @Query("SELECT d FROM DealEntity d WHERE d.startDate BETWEEN :startDate AND :endDate")
  List<DealEntity> findDealsBetweenDates(@Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);

  @Query("SELECT d FROM DealEntity d WHERE d.customerCompany.id = :customer_id AND d.dealStatus IN :statuses")
  List<DealEntity> findByCustomerAndStatuses(@Param("customer_id") String customerId,
      @Param("statuses") List<DealStatus> statuses);

  @Query("SELECT COUNT(d) FROM DealEntity d WHERE d.dealStatus = :status")
  long countByStatus(@Param("status") DealStatus status);

  @Query("SELECT d FROM DealEntity d WHERE d.dealStatus NOT IN (:excludedStatuses)")
  List<DealEntity> findActiveDeals(@Param("excludedStatuses") List<DealStatus> excludedStatuses);

  boolean existsByOpportunityId(String opportunityId);
}
