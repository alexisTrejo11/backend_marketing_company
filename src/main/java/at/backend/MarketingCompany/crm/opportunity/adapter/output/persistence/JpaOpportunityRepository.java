package at.backend.MarketingCompany.crm.opportunity.adapter.output.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityStage;

import java.util.List;
import java.util.Set;

public interface JpaOpportunityRepository extends JpaRepository<OpportunityEntity, Long> {

  @Query("SELECT o FROM OpportunityEntity o WHERE o.expectedCloseDate < CURRENT_DATE AND o.stage NOT IN ('CLOSED_WON', 'CLOSED_LOST')")
  Page<OpportunityEntity> findOverdue(Pageable pageable);

  Page<OpportunityEntity> findByStageIn(Set<OpportunityStage> stages, Pageable pageable);

  Page<OpportunityEntity> findByCustomerCompanyId(Long customerId, Pageable pageable);

  List<OpportunityEntity> findByCustomerCompanyId(Long customerId);

  Page<OpportunityEntity> findByStage(OpportunityStage stage, Pageable pageable);

  long countByCustomerCompanyIdAndStage(Long customerId, OpportunityStage stage);

  long countByCustomerCompanyIdAndStageIn(Long customerId, Set<OpportunityStage> stages);
}
