package at.backend.MarketingCompany.crm.opportunity.infrastructure.persistence;

import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityStage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface JpaOpportunityRepository extends JpaRepository<OpportunityEntity, String> {

    @Query("SELECT o FROM OpportunityEntity o WHERE o.expectedCloseDate < CURRENT_DATE AND o.stage NOT IN ('CLOSED_WON', 'CLOSED_LOST')")
    Page<OpportunityEntity> findOverdue(Pageable pageable);

    Page<OpportunityEntity> findByStageIn(Set<OpportunityStage> stages, Pageable pageable);

    Page<OpportunityEntity> findByCustomerCompanyId(String customerId, Pageable pageable);

    List<OpportunityEntity> findByCustomerCompanyId(String customerId);

    Page<OpportunityEntity> findByStage(OpportunityStage stage, Pageable pageable);

    long countByCustomerCompanyIdAndStage(String customerId, OpportunityStage stage);

    long countByCustomerCompanyIdAndStageIn(String customerId, Set<OpportunityStage> stages);
}
