package at.backend.MarketingCompany.crm.deal.core.application;

import at.backend.MarketingCompany.crm.deal.core.application.commands.*;

import at.backend.MarketingCompany.crm.deal.core.domain.entity.Deal;
import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.CreateDealParams;
import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.DealId;
import at.backend.MarketingCompany.crm.deal.core.domain.exceptions.DealNotFoundException;
import at.backend.MarketingCompany.crm.deal.core.port.output.DealRepository;
import at.backend.MarketingCompany.crm.deal.core.port.input.DealCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DealCommandServiceImpl implements DealCommandService {
  private final DealRepository dealRepository;
  private final ExternalModuleValidator externalValidator;

  @Override
  @Transactional
  public Deal createDeal(CreateDealCommand command) {
    log.info("Creating deal for opportunity: {}", command.opportunityId());

    externalValidator.validateCustomerExists(command.customerCompanyId());
    externalValidator.validateOpportunityExists(command.opportunityId());
    externalValidator.validateServicesExist(command.servicePackageIds());
    log.info("Validations passed for creating deal for opportunity: {}", command.opportunityId());

    CreateDealParams createParams = command.toCreateParams();
    Deal newDeal = Deal.create(createParams);
    log.info("Deal created in domain layer for opportunity: {}", command.opportunityId());

    Deal savedDeal = dealRepository.save(newDeal);
    log.info("Deal created successfully with ID: {}", savedDeal.getId().getValue());

    return savedDeal;
  }

  @Override
  @Transactional
  public Deal signDeal(SignDealCommand command) {
    log.info("Signing deal: {}", command.dealId());

    Deal deal = findDealOrThrow(command.dealId());
    externalValidator.validateEmployeeExists(command.campaignManagerId());
    log.debug("Signing deal {} with final amount: {}, terms: {}, campaign manager ID: {}",
        command.dealId(), command.finalAmount(), command.terms(), command.campaignManagerId());

    deal.signDeal(command.finalAmount(), command.terms(), command.campaignManagerId());
    log.debug("Deal {} signed in domain layer", command.dealId());

    Deal updatedDeal = dealRepository.save(deal);
    log.info("Deal {} signed successfully", command.dealId());

    return updatedDeal;
  }

  @Override
  @Transactional
  public Deal markDealAsPaid(MarkDealAsPaidCommand command) {
    log.info("Marking deal as paid: {}", command.dealId());

    Deal deal = findDealOrThrow(command.dealId());
    deal.markAsPaid();

    Deal updatedDeal = dealRepository.save(deal);
    log.info("Deal {} marked as paid", command.dealId());

    return updatedDeal;
  }

  @Override
  @Transactional
  public Deal startDealExecution(StartDealExecutionCommand command) {
    log.info("Starting deal execution: {}", command.dealId());

    Deal deal = findDealOrThrow(command.dealId());
    deal.startExecution();

    Deal updatedDeal = dealRepository.save(deal);
    log.info("Deal {} execution started", command.dealId());

    return updatedDeal;
  }

  @Override
  @Transactional
  public Deal completeDeal(CompleteDealCommand command) {
    log.info("Completing deal: {}", command.dealId());

    Deal deal = findDealOrThrow(command.dealId());
    deal.completeDeal(command.endDate(), command.deliverables());

    Deal updatedDeal = dealRepository.save(deal);
    log.info("Deal {} completed successfully", command.dealId());

    return updatedDeal;
  }

  @Override
  @Transactional
  public Deal cancelDeal(CancelDealCommand command) {
    log.info("Cancelling deal: {}", command.dealId());

    Deal deal = findDealOrThrow(command.dealId());
    deal.cancelDeal();

    Deal updatedDeal = dealRepository.save(deal);
    log.info("Deal {} cancelled", command.dealId());

    return updatedDeal;
  }

  @Override
  @Transactional
  public Deal updateDealServices(UpdateDealServicesCommand command) {
    log.info("Updating services for deal: {}", command.dealId());

    Deal deal = findDealOrThrow(command.dealId());
    externalValidator.validateServicesExist(command.servicePackageIds());

    deal.updateServicePackages(command.servicePackageIds());

    Deal updatedDeal = dealRepository.save(deal);
    log.info("Services updated for deal {}", command.dealId());

    return updatedDeal;
  }

  private Deal findDealOrThrow(DealId dealId) {
    return dealRepository.findById(dealId)
        .orElseThrow(() -> new DealNotFoundException(dealId));
  }
}
