package at.backend.MarketingCompany.crm.opportunity.core.port.output;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.Opportunity;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityStage;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;

public interface OpportunityRepository {
  Opportunity save(Opportunity opportunity);

  Optional<Opportunity> findById(OpportunityId opportunityId);

  void delete(Opportunity opportunity);

  boolean existsById(OpportunityId opportunityId);

  Page<Opportunity> findByCustomer(CustomerCompanyId customerCompanyId, Pageable pageable);

  List<Opportunity> findByCustomer(CustomerCompanyId customerCompanyId);

  Page<Opportunity> findByStage(OpportunityStage stage, Pageable pageable);

  Page<Opportunity> findByStages(Set<OpportunityStage> stages, Pageable pageable);

  Page<Opportunity> findActiveOpportunities(Pageable pageable);

  Page<Opportunity> findOverdueOpportunities(Pageable pageable);

  Page<Opportunity> findWonOpportunities(Pageable pageable);

  Page<Opportunity> findLostOpportunities(Pageable pageable);

  long countByCustomerAndStage(CustomerCompanyId customerCompanyId, OpportunityStage stage);

  long countActiveByCustomer(CustomerCompanyId customerCompanyId);

  double calculateWinRateByCustomer(CustomerCompanyId customerCompanyId);

  /**
   * Count total opportunities (all customers)
   */
  long count();

  /**
   * Count active opportunities (all customers)
   */
  long countActive();

  /**
   * Count opportunities by stage (all customers)
   */
  long countByStage(OpportunityStage stage);

  /**
   * Calculate global win rate (all customers)
   */
  double calculateWinRate();

  /**
   * Calculate total pipeline value
   * 
   * @param customerCompanyId Optional customer filter (null for all)
   */
  double calculateTotalPipelineValue(CustomerCompanyId customerCompanyId);

  /**
   * Calculate average deal size
   * 
   * @param customerCompanyId Optional customer filter (null for all)
   */
  double calculateAverageDealSize(CustomerCompanyId customerCompanyId);
}
