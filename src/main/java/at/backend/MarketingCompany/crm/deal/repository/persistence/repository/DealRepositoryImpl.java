package at.backend.MarketingCompany.crm.deal.repository.persistence.repository;

import at.backend.MarketingCompany.crm.Utils.enums.DealStatus;
import at.backend.MarketingCompany.crm.deal.domain.entity.Deal;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.DealId;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.*;
import at.backend.MarketingCompany.crm.deal.domain.respository.DealRepository;
import at.backend.MarketingCompany.crm.deal.repository.persistence.model.DealEntity;
import at.backend.MarketingCompany.crm.deal.repository.persistence.model.DealEntityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    public List<Deal> findByCustomer(CustomerId customerId) {
        log.debug("Finding deals by customer ID: {}", customerId.value());

        return jpaDealRepository.findByCustomerModelId(customerId.value().toString()).stream()
                .map(dealEntityMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Deal> findByStatuses(List<DealStatus> statuses) {
        log.debug("Finding deals by statuses: {}", statuses);

        return jpaDealRepository.findByDealStatusIn(statuses).stream()
                .map(dealEntityMapper::toDomain)
                .toList();
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
    public List<Deal> findByOpportunity(OpportunityId opportunityId) {
        log.debug("Finding deals by opportunity ID: {}", opportunityId.value());

        return jpaDealRepository.findByOpportunityId(opportunityId.value().toString()).stream()
                .map(dealEntityMapper::toDomain)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Deal> findByCampaignManager(EmployeeId employeeId) {
        log.debug("Finding deals by campaign manager ID: {}", employeeId.value());

        return jpaDealRepository.findByCampaignManagerId(employeeId.value().toString()).stream()
                .map(dealEntityMapper::toDomain)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Deal> findActiveDeals() {
        log.debug("Finding active deals");

        List<DealStatus> activeStatuses = List.of(
                DealStatus.DRAFT,
                DealStatus.IN_NEGOTIATION,
                DealStatus.SIGNED,
                DealStatus.PAID,
                DealStatus.IN_PROGRESS
        );

        return jpaDealRepository.findByDealStatusIn(activeStatuses).stream()
                .map(dealEntityMapper::toDomain)
                .toList();
    }
}