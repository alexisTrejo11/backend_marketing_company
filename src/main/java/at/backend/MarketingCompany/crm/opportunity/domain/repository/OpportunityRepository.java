package at.backend.MarketingCompany.crm.opportunity.domain.repository;

import at.backend.MarketingCompany.crm.opportunity.domain.entity.Opportunity;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityStage;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerId;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OpportunityRepository {
  Opportunity save(Opportunity opportunity);

  Optional<Opportunity> findById(OpportunityId opportunityId);

  void delete(Opportunity opportunity);

  boolean existsById(OpportunityId opportunityId);

  Page<Opportunity> findByCustomer(CustomerId customerId, Pageable pageable);

  List<Opportunity> findByCustomer(CustomerId customerId);

  Page<Opportunity> findByStage(OpportunityStage stage, Pageable pageable);

  Page<Opportunity> findByStages(Set<OpportunityStage> stages, Pageable pageable);

  Page<Opportunity> findActiveOpportunities(Pageable pageable);

  Page<Opportunity> findOverdueOpportunities(Pageable pageable);

  Page<Opportunity> findWonOpportunities(Pageable pageable);

  Page<Opportunity> findLostOpportunities(Pageable pageable);

  long countByCustomerAndStage(CustomerId customerId, OpportunityStage stage);

  long countActiveByCustomer(CustomerId customerId);

  double calculateWinRateByCustomer(CustomerId customerId);
}
