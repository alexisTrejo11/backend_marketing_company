package at.backend.MarketingCompany.crm.deal.core.application;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.backend.MarketingCompany.crm.deal.core.application.queries.*;
import at.backend.MarketingCompany.crm.deal.core.domain.entity.Deal;
import at.backend.MarketingCompany.crm.deal.core.domain.exceptions.DealNotFoundException;
import at.backend.MarketingCompany.crm.deal.core.domain.respository.DealRepository;
import at.backend.MarketingCompany.crm.deal.core.port.input.DealQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class DealQueryServiceImpl implements DealQueryService {
  private final DealRepository dealRepository;

  @Override
  @Transactional(readOnly = true)
  public Deal getDealById(GetDealByIdQuery query) {
    log.debug("Fetching deal by ID: {}", query.dealId());

    return dealRepository.findById(query.dealId())
        .orElseThrow(() -> new DealNotFoundException(query.dealId()));
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Deal> getDealsByStatus(GetDealsByStatusQuery query) {
    log.debug("Fetching deals by status: {}", query.statuses());

    return dealRepository.findByStatuses(query.statuses(), query.pageable());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Deal> getDealsByCustomer(GetDealsByCustomerQuery query) {
    log.debug("Fetching deals for customer: {}", query.customerCompanyId());

    return dealRepository.findByCustomer(query.customerCompanyId(), query.pageable());
  }

  @Override
  public Page<Deal> getAllDeals(GetAllDealsQuery query) {
    log.debug("Fetching all deals");

    return dealRepository.findAll(query.pageable());
  }

}
