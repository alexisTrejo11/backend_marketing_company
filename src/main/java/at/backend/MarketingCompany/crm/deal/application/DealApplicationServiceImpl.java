package at.backend.MarketingCompany.crm.deal.application;

import at.backend.MarketingCompany.crm.deal.application.commands.*;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.CreateDealParams;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.DealId;
import at.backend.MarketingCompany.crm.deal.application.queries.GetDealByIdQuery;
import at.backend.MarketingCompany.crm.deal.application.queries.GetDealsByCustomerQuery;
import at.backend.MarketingCompany.crm.deal.application.queries.GetDealsByStatusQuery;
import at.backend.MarketingCompany.crm.deal.domain.entity.Deal;
import at.backend.MarketingCompany.crm.deal.domain.exceptions.DealNotFoundException;
import at.backend.MarketingCompany.crm.deal.domain.respository.DealRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DealApplicationServiceImpl implements DealApplicationService {
    private final DealRepository dealRepository;
    private final ExternalModuleValidator externalValidator;

    @Transactional
    public Deal handle(CreateDealCommand command) {
        log.info("Creating deal for opportunity: {}", command.opportunityId());
        
        externalValidator.validateCustomerExists(command.customerId());
        externalValidator.validateOpportunityExists(command.opportunityId());
        externalValidator.validateServicesExist(command.servicePackageIds());
        
        var createParams = CreateDealParams.builder()
            .customerId(command.customerId())
            .opportunityId(command.opportunityId())
            .startDate(command.startDate())
            .servicePackageIds(command.servicePackageIds())
            .build();
        
        Deal newDeal = Deal.create(createParams);
        Deal savedDeal = dealRepository.save(newDeal);
        
        log.info("Deal created successfully with ID: {}", savedDeal.getId().value());
        return savedDeal;

    }

    @Transactional
    public Deal handle(SignDealCommand command) {
        log.info("Signing deal: {}", command.dealId());
        
        Deal deal = findDealOrThrow(command.dealId());
        externalValidator.validateEmployeeExists(command.campaignManagerId());
        
        deal.signDeal(
            command.finalAmount(),
            command.terms(),
            command.campaignManagerId()
        );
        
        Deal updatedDeal = dealRepository.save(deal);
        log.info("Deal {} signed successfully", command.dealId());

        return updatedDeal;

    }
    
    @Transactional
    public Deal handle(MarkDealAsPaidCommand command) {
        log.info("Marking deal as paid: {}", command.dealId());
        
        Deal deal = findDealOrThrow(command.dealId());
        deal.markAsPaid();
        
        Deal updatedDeal = dealRepository.save(deal);
        log.info("Deal {} marked as paid", command.dealId());

        return updatedDeal;
    }
    
    @Transactional
    public Deal handle(StartDealExecutionCommand command) {
        log.info("Starting deal execution: {}", command.dealId());
        
        Deal deal = findDealOrThrow(command.dealId());
        deal.startExecution();
        
        Deal updatedDeal = dealRepository.save(deal);
        log.info("Deal {} execution started", command.dealId());

        return updatedDeal;
    }


    @Transactional
    public Deal handle(CompleteDealCommand command) {
        log.info("Completing deal: {}", command.dealId());
        
        Deal deal = findDealOrThrow(command.dealId());
        deal.completeDeal(command.endDate(), command.deliverables());
        
        Deal updatedDeal = dealRepository.save(deal);
        log.info("Deal {} completed successfully", command.dealId());

        return updatedDeal;
    }
    
    @Transactional
    public Deal handle(CancelDealCommand command) {
        log.info("Cancelling deal: {}", command.dealId());
        
        Deal deal = findDealOrThrow(command.dealId());
        deal.cancelDeal();
        
        Deal updatedDeal = dealRepository.save(deal);
        log.info("Deal {} cancelled", command.dealId());

        return updatedDeal;
    }

    
    @Transactional
    public Deal handle(UpdateDealServicesCommand command) {
        log.info("Updating services for deal: {}", command.dealId());
        
        Deal deal = findDealOrThrow(command.dealId());
        externalValidator.validateServicesExist(command.servicePackageIds());

        deal.updateServicePackages(command.servicePackageIds());
        
        Deal updatedDeal = dealRepository.save(deal);
        log.info("Services updated for deal {}", command.dealId());

        return updatedDeal;
    }

    

    @Transactional(readOnly = true)
    public Deal handle(GetDealByIdQuery query) {
        log.debug("Fetching deal by ID: {}", query.dealId());
        
        return dealRepository.findById(query.dealId())
            .orElseThrow(() -> new DealNotFoundException(query.dealId()));
    }
    
    @Transactional(readOnly = true)
    public List<Deal> handle(GetDealsByStatusQuery query) {
        log.debug("Fetching deals by status: {}", query.statuses());
        
        return dealRepository.findByStatuses(query.statuses()).stream()
            .toList();
    }
    
    @Transactional(readOnly = true)
    public List<Deal> handle(GetDealsByCustomerQuery query) {
        log.debug("Fetching deals for customer: {}", query.customerId());
        
        return dealRepository.findByCustomer(query.customerId()).stream()
            .toList();
    }
    

    private Deal findDealOrThrow(DealId dealId) {
        return dealRepository.findById(dealId)
            .orElseThrow(() -> new DealNotFoundException(dealId));
    }
}