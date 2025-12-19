package at.backend.MarketingCompany.crm.opportunity.adapter.output.persistence;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.Opportunity;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityStage;
import at.backend.MarketingCompany.crm.opportunity.core.port.output.OpportunityRepository;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Repository
@RequiredArgsConstructor
public class OpportunityRepositoryImpl implements OpportunityRepository {
  private final JpaOpportunityRepository jpaOpportunityRepository;
  private final OpportunityEntityMapper opportunityEntityMapper;

  @Override
  @Transactional
  public Opportunity save(Opportunity opportunity) {
    log.debug("Saving opportunity with ID: {}", opportunity.getId().getValue());

    OpportunityEntity entity = opportunityEntityMapper.toEntity(opportunity);
    OpportunityEntity savedEntity = jpaOpportunityRepository.save(entity);

    log.info("Opportunity saved successfully with ID: {}", savedEntity.getId());
    return opportunityEntityMapper.toDomain(savedEntity);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Opportunity> findById(OpportunityId opportunityId) {
    log.debug("Finding opportunity by ID: {}", opportunityId.getValue());

    return jpaOpportunityRepository.findById(opportunityId.getValue())
        .map(opportunityEntityMapper::toDomain);
  }

  @Override
  @Transactional
  public void delete(Opportunity opportunity) {
    log.debug("Deleting opportunity with ID: {}", opportunity.getId().getValue());

    OpportunityEntity entity = opportunityEntityMapper.toEntity(opportunity);
    jpaOpportunityRepository.delete(entity);

    log.info("Opportunity deleted successfully with ID: {}", opportunity.getId().getValue());
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsById(OpportunityId opportunityId) {
    return jpaOpportunityRepository.existsById(opportunityId.getValue());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Opportunity> findByCustomer(CustomerCompanyId customerCompanyId, Pageable pageable) {
    log.debug("Finding paginated opportunities by customer ID: {}", customerCompanyId.getValue());

    return jpaOpportunityRepository.findByCustomerCompanyId(customerCompanyId.getValue(), pageable)
        .map(opportunityEntityMapper::toDomain);
  }

  @Override
  public List<Opportunity> findByCustomer(CustomerCompanyId customerCompanyId) {
    log.debug("Finding all opportunities by customer ID: {}", customerCompanyId.getValue());

    return jpaOpportunityRepository.findByCustomerCompanyId(customerCompanyId.getValue()).stream()
        .map(opportunityEntityMapper::toDomain)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Opportunity> findByStage(OpportunityStage stage, Pageable pageable) {
    log.debug("Finding opportunities by stage: {}", stage);

    return jpaOpportunityRepository.findByStage(stage, pageable)
        .map(opportunityEntityMapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Opportunity> findByStages(Set<OpportunityStage> stages, Pageable pageable) {
    log.debug("Finding opportunities by stages: {}", stages);

    return jpaOpportunityRepository.findByStageIn(stages, pageable)
        .map(opportunityEntityMapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Opportunity> findActiveOpportunities(Pageable pageable) {
    log.debug("Finding active opportunities");

    Set<OpportunityStage> activeStages = Set.of(
        OpportunityStage.PROSPECTING,
        OpportunityStage.QUALIFIED,
        OpportunityStage.PROPOSAL,
        OpportunityStage.NEGOTIATION);

    return jpaOpportunityRepository.findByStageIn(activeStages, pageable)
        .map(opportunityEntityMapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Opportunity> findOverdueOpportunities(Pageable pageable) {
    log.debug("Finding overdue opportunities");

    return jpaOpportunityRepository.findOverdue(pageable)
        .map(opportunityEntityMapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Opportunity> findWonOpportunities(Pageable pageable) {
    log.debug("Finding won opportunities");

    return jpaOpportunityRepository.findByStage(OpportunityStage.CLOSED_WON, pageable)
        .map(opportunityEntityMapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Opportunity> findLostOpportunities(Pageable pageable) {
    log.debug("Finding lost opportunities");

    return jpaOpportunityRepository.findByStage(OpportunityStage.CLOSED_LOST, pageable)
        .map(opportunityEntityMapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public long countByCustomerAndStage(CustomerCompanyId customerCompanyId, OpportunityStage stage) {
    log.debug("Counting opportunities for customer {} with stage: {}", customerCompanyId.getValue(), stage);

    return jpaOpportunityRepository.countByCustomerCompanyIdAndStage(customerCompanyId.getValue(), stage);
  }

  @Override
  @Transactional(readOnly = true)
  public long countActiveByCustomer(CustomerCompanyId customerCompanyId) {
    log.debug("Counting active opportunities for customer: {}", customerCompanyId.getValue());

    Set<OpportunityStage> activeStages = Set.of(
        OpportunityStage.PROSPECTING,
        OpportunityStage.QUALIFIED,
        OpportunityStage.PROPOSAL,
        OpportunityStage.NEGOTIATION);

    return jpaOpportunityRepository.countByCustomerCompanyIdAndStageIn(customerCompanyId.getValue(), activeStages);
  }

  @Override
  @Transactional(readOnly = true)
  public double calculateWinRateByCustomer(CustomerCompanyId customerCompanyId) {
    log.debug("Calculating win rate for customer: {}", customerCompanyId.getValue());

    long totalClosed = jpaOpportunityRepository.countByCustomerCompanyIdAndStageIn(
        customerCompanyId.getValue(),
        Set.of(OpportunityStage.CLOSED_WON, OpportunityStage.CLOSED_LOST));

    if (totalClosed == 0) {
      return 0.0;
    }

    long wonCount = jpaOpportunityRepository.countByCustomerCompanyIdAndStage(
        customerCompanyId.getValue(),
        OpportunityStage.CLOSED_WON);

    return (double) wonCount / totalClosed * 100;
  }
}
