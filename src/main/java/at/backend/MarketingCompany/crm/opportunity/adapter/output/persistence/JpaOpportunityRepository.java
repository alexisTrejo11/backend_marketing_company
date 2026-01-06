package at.backend.MarketingCompany.crm.opportunity.adapter.output.persistence;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityStage;

public interface JpaOpportunityRepository extends JpaRepository<OpportunityEntity, Long> {

  @Query("SELECT o FROM OpportunityEntity o WHERE o.expectedCloseDate < CURRENT_DATE AND o.stage NOT IN ('CLOSED_WON', 'CLOSED_LOST')")
  Page<OpportunityEntity> findOverdue(Pageable pageable);

  Page<OpportunityEntity> findByStageIn(Set<OpportunityStage> stages, Pageable pageable);

  Page<OpportunityEntity> findByCustomerCompany_Id(Long customerId, Pageable pageable);

  List<OpportunityEntity> findByCustomerCompany_Id(Long customerId);

  Page<OpportunityEntity> findByStage(OpportunityStage stage, Pageable pageable);

  long countByCustomerCompany_IdAndStage(Long customerId, OpportunityStage stage);

  long countByCustomerCompany_IdAndStageIn(Long customerId, Set<OpportunityStage> stages);

  long countByStageIn(Set<OpportunityStage> stages);

  long countByStage(OpportunityStage stage);

  @Query("SELECT COALESCE(SUM(o.amount), 0) FROM OpportunityEntity o WHERE o.customerCompany.id = :customerId AND o.stage IN :stages")
  double sumAmountByCustomerAndStages(@Param("customerId") Long customerId,
      @Param("stages") Set<OpportunityStage> stages);

  @Query("SELECT COALESCE(SUM(o.amount), 0) FROM OpportunityEntity o WHERE o.stage IN :stages")
  double sumAmountByStages(@Param("stages") Set<OpportunityStage> stages);

  @Query("SELECT AVG(o.amount) FROM OpportunityEntity o WHERE o.customerCompany.id = :customerId AND o.amount IS NOT NULL")
  Double averageAmountByCustomer(@Param("customerId") Long customerId);

  @Query("SELECT AVG(o.amount) FROM OpportunityEntity o WHERE o.amount IS NOT NULL")
  Double averageAmount();
}
