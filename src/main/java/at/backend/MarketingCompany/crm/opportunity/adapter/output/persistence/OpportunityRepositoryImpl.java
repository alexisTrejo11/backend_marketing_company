package at.backend.MarketingCompany.crm.opportunity.adapter.output.persistence;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.Opportunity;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityStage;
import at.backend.MarketingCompany.crm.opportunity.core.port.output.OpportunityRepository;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
    entity.processNewEntityIfNeeded();

    OpportunityEntity savedEntity = jpaOpportunityRepository.saveAndFlush(entity);

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

    return jpaOpportunityRepository.findByCustomerCompany_Id(customerCompanyId.getValue(), pageable)
        .map(opportunityEntityMapper::toDomain);
  }

  @Override
  public List<Opportunity> findByCustomer(CustomerCompanyId customerCompanyId) {
    log.debug("Finding all opportunities by customer ID: {}", customerCompanyId.getValue());

    return jpaOpportunityRepository.findByCustomerCompany_Id(customerCompanyId.getValue()).stream()
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
        OpportunityStage.QUALIFICATION,
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

    return jpaOpportunityRepository.countByCustomerCompany_IdAndStage(customerCompanyId.getValue(), stage);
  }

  @Override
  @Transactional(readOnly = true)
  public long countActiveByCustomer(CustomerCompanyId customerCompanyId) {
    log.debug("Counting active opportunities for customer: {}", customerCompanyId.getValue());

    Set<OpportunityStage> activeStages = Set.of(
        OpportunityStage.PROSPECTING,
        OpportunityStage.QUALIFICATION,
        OpportunityStage.PROPOSAL,
        OpportunityStage.NEGOTIATION);

    return jpaOpportunityRepository.countByCustomerCompany_IdAndStageIn(customerCompanyId.getValue(), activeStages);
  }

  @Override
  @Transactional(readOnly = true)
  public double calculateWinRateByCustomer(CustomerCompanyId customerCompanyId) {
    log.debug("Calculating win rate for customer: {}", customerCompanyId.getValue());

    long totalClosed = jpaOpportunityRepository.countByCustomerCompany_IdAndStageIn(
        customerCompanyId.getValue(),
        Set.of(OpportunityStage.CLOSED_WON, OpportunityStage.CLOSED_LOST));

    if (totalClosed == 0) {
      return 0.0;
    }

    long wonCount = jpaOpportunityRepository.countByCustomerCompany_IdAndStage(
        customerCompanyId.getValue(),
        OpportunityStage.CLOSED_WON);

    return (double) wonCount / totalClosed * 100;
  }

  @Override
  @Transactional(readOnly = true)
  public long count() {
    log.debug("Counting all opportunities");
    return jpaOpportunityRepository.count();
  }

  @Override
  @Transactional(readOnly = true)
  public long countActive() {
    log.debug("Counting all active opportunities");

    Set<OpportunityStage> activeStages = Set.of(
        OpportunityStage.PROSPECTING,
        OpportunityStage.QUALIFICATION,
        OpportunityStage.PROPOSAL,
        OpportunityStage.NEGOTIATION);

    return jpaOpportunityRepository.countByStageIn(activeStages);
  }

  @Override
  @Transactional(readOnly = true)
  public long countByStage(OpportunityStage stage) {
    log.debug("Counting opportunities by stage: {}", stage);
    return jpaOpportunityRepository.countByStage(stage);
  }

  @Override
  @Transactional(readOnly = true)
  public double calculateWinRate() {
    log.debug("Calculating global win rate");

    long totalClosed = jpaOpportunityRepository.countByStageIn(
        Set.of(OpportunityStage.CLOSED_WON, OpportunityStage.CLOSED_LOST));

    if (totalClosed == 0) {
      return 0.0;
    }

    long wonCount = jpaOpportunityRepository.countByStage(OpportunityStage.CLOSED_WON);
    return (double) wonCount / totalClosed * 100;
  }

  @Override
  @Transactional(readOnly = true)
  public double calculateTotalPipelineValue(CustomerCompanyId customerCompanyId) {
    log.debug("Calculating total pipeline value for customer: {}", customerCompanyId);

    Set<OpportunityStage> activeStages = Set.of(
        OpportunityStage.PROSPECTING,
        OpportunityStage.QUALIFICATION,
        OpportunityStage.PROPOSAL,
        OpportunityStage.NEGOTIATION);

    if (customerCompanyId != null) {
      return jpaOpportunityRepository.sumAmountByCustomerAndStages(
          customerCompanyId.getValue(), activeStages);
    } else {
      return jpaOpportunityRepository.sumAmountByStages(activeStages);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public double calculateAverageDealSize(CustomerCompanyId customerCompanyId) {
    log.debug("Calculating average deal size for customer: {}", customerCompanyId);

    Double average;
    if (customerCompanyId != null) {
      average = jpaOpportunityRepository.averageAmountByCustomer(customerCompanyId.getValue());
    } else {
      average = jpaOpportunityRepository.averageAmount();
    }

    return average != null ? average : 0.0;
  }
}
