package at.backend.MarketingCompany.crm.deal.adapter.output.persistence.repository;

import at.backend.MarketingCompany.crm.shared.enums.DealStatus;
import at.backend.MarketingCompany.crm.deal.adapter.output.persistence.model.DealEntity;
import at.backend.MarketingCompany.crm.deal.adapter.output.persistence.model.DealEntityMapper;
import at.backend.MarketingCompany.crm.deal.core.domain.entity.Deal;
import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.DealId;
import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.external.*;
import at.backend.MarketingCompany.crm.deal.core.domain.respository.DealRepository;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;
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
public class DealRepositoryImpl implements DealRepository {
  private final JpaDealRepository jpaDealRepository;
  private final DealEntityMapper dealEntityMapper;

  @Override
  @Transactional
  public Deal save(Deal deal) {
    log.debug("Saving deal with ID: {}", deal.getId().value());

    DealEntity entity = dealEntityMapper.toEntity(deal);

    DealEntity savedEntity = jpaDealRepository.save(entity);
    log.info("Deal saved successfully with ID: {}", savedEntity.getId());

    return dealEntityMapper.toDomain(savedEntity);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Deal> findById(DealId dealId) {
    log.debug("Finding deal by ID: {}", dealId.value());
    return jpaDealRepository.findById(dealId.value())
        .map(dealEntityMapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Deal> findByCustomer(CustomerCompanyId customerCompanyId, Pageable pageable) {
    log.debug("Finding deals by customer ID: {}", customerCompanyId.value());

    return jpaDealRepository.findByCustomerId(customerCompanyId.value(), pageable)
        .map(dealEntityMapper::toDomain);

  }

  @Override
  @Transactional(readOnly = true)
  public Page<Deal> findByStatuses(Set<DealStatus> statuses, Pageable pageable) {
    log.debug("Finding deals by statuses: {}", statuses);

    return jpaDealRepository.findByDealStatusIn(statuses, pageable)
        .map(dealEntityMapper::toDomain);

  }

  @Override
  @Transactional
  public void delete(Deal deal) {
    log.debug("Deleting deal with ID: {}", deal.getId().value());

    DealEntity entity = dealEntityMapper.toEntity(deal);
    jpaDealRepository.delete(entity);

    log.info("Deal deleted successfully with ID: {}", deal.getId().value());
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsById(DealId dealId) {
    return jpaDealRepository.existsById(dealId.value());
  }

  @Transactional(readOnly = true)
  public Page<Deal> findByOpportunity(OpportunityId opportunityId, Pageable pageable) {
    log.debug("Finding deals by opportunity ID: {}", opportunityId.value());

    return jpaDealRepository.findByOpportunityId(opportunityId.value(), pageable)
        .map(dealEntityMapper::toDomain);
  }

  @Transactional(readOnly = true)
  public Page<Deal> findByCampaignManager(EmployeeId employeeId, Pageable pageable) {
    log.debug("Finding deals by campaign manager ID: {}", employeeId.value());

    return jpaDealRepository.findByCampaignManagerId(employeeId.value(), pageable)
        .map(dealEntityMapper::toDomain);
  }

  @Transactional(readOnly = true)
  public Page<Deal> findActiveDeals(Pageable pageable) {
    log.debug("Finding active deals");

    Set<DealStatus> activeStatuses = Set.of(
        DealStatus.DRAFT,
        DealStatus.IN_NEGOTIATION,
        DealStatus.SIGNED,
        DealStatus.PAID,
        DealStatus.IN_PROGRESS);

    return jpaDealRepository.findByDealStatusIn(activeStatuses, pageable)
        .map(dealEntityMapper::toDomain);
  }

  @Override
  public Page<Deal> findAll(Pageable pageable) {
    log.debug("Finding all deals with pagination");

    return jpaDealRepository.findAll(pageable)
        .map(dealEntityMapper::toDomain);
  }
}
