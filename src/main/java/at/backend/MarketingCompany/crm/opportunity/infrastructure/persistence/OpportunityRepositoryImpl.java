package at.backend.MarketingCompany.crm.opportunity.infrastructure.persistence;

import at.backend.MarketingCompany.crm.opportunity.domain.entity.Opportunity;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityStage;
import at.backend.MarketingCompany.crm.opportunity.domain.repository.OpportunityRepository;
import at.backend.MarketingCompany.customer.domain.ValueObjects.CustomerId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
        log.debug("Saving opportunity with ID: {}", opportunity.getId().value());

        OpportunityEntity entity = opportunityEntityMapper.toEntity(opportunity);
        OpportunityEntity savedEntity = jpaOpportunityRepository.save(entity);

        log.info("Opportunity saved successfully with ID: {}", savedEntity.getId());
        return opportunityEntityMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Opportunity> findById(OpportunityId opportunityId) {
        log.debug("Finding opportunity by ID: {}", opportunityId.value());

        return jpaOpportunityRepository.findById(opportunityId.value())
                .map(opportunityEntityMapper::toDomain);
    }

    @Override
    @Transactional
    public void delete(Opportunity opportunity) {
        log.debug("Deleting opportunity with ID: {}", opportunity.getId().value());

        OpportunityEntity entity = opportunityEntityMapper.toEntity(opportunity);
        jpaOpportunityRepository.delete(entity);

        log.info("Opportunity deleted successfully with ID: {}", opportunity.getId().value());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(OpportunityId opportunityId) {
        return jpaOpportunityRepository.existsById(opportunityId.value());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Opportunity> findByCustomer(CustomerId customerId, Pageable pageable) {
        log.debug("Finding opportunities by customer ID: {}", customerId.value());

        return jpaOpportunityRepository.findByCustomerId(customerId.value(), pageable)
                .map(opportunityEntityMapper::toDomain);
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
            OpportunityStage.LEAD,
            OpportunityStage.QUALIFIED,
            OpportunityStage.PROPOSAL,
            OpportunityStage.NEGOTIATION
        );

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
    public long countByCustomerAndStage(CustomerId customerId, OpportunityStage stage) {
        log.debug("Counting opportunities for customer {} with stage: {}", customerId.value(), stage);

        return jpaOpportunityRepository.countByCustomerIdAndStage(customerId.value(), stage);
    }

    @Override
    @Transactional(readOnly = true)
    public long countActiveByCustomer(CustomerId customerId) {
        log.debug("Counting active opportunities for customer: {}", customerId.value());

        Set<OpportunityStage> activeStages = Set.of(
            OpportunityStage.LEAD,
            OpportunityStage.QUALIFIED,
            OpportunityStage.PROPOSAL,
            OpportunityStage.NEGOTIATION
        );

        return jpaOpportunityRepository.countByCustomerModelIdAndStageIn(customerId.value(), activeStages);
    }

    @Override
    @Transactional(readOnly = true)
    public double calculateWinRateByCustomer(CustomerId customerId) {
        log.debug("Calculating win rate for customer: {}", customerId.value());

        long totalClosed = jpaOpportunityRepository.countByCustomerModelIdAndStageIn(
            customerId.value(),
            Set.of(OpportunityStage.CLOSED_WON, OpportunityStage.CLOSED_LOST)
        );

        if (totalClosed == 0) {
            return 0.0;
        }

        long wonCount = jpaOpportunityRepository.countByCustomerIdAndStage(
            customerId.value(),
            OpportunityStage.CLOSED_WON
        );

        return (double) wonCount / totalClosed * 100;
    }
}