package at.backend.MarketingCompany.crm.opportunity.application;

import at.backend.MarketingCompany.crm.deal.application.ExternalModuleValidator;
import at.backend.MarketingCompany.crm.opportunity.application.commands.*;
import at.backend.MarketingCompany.crm.opportunity.application.queries.*;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.Opportunity;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.*;
import at.backend.MarketingCompany.crm.opportunity.domain.exceptions.OpportunityNotFoundException;
import at.backend.MarketingCompany.crm.opportunity.domain.repository.OpportunityRepository;
import at.backend.MarketingCompany.shared.domain.exceptions.ExternalServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpportunityApplicationService {
    
    private final OpportunityRepository opportunityRepository;
    private final ExternalModuleValidator externalValidator;

    @Transactional
    public Opportunity handle(CreateOpportunityCommand command) {
        log.info("Creating opportunity for customer: {}", command.customerCompanyId().value());
        
        validateExternalDependencies(command);
        
        var createParams = CreateOpportunityParams.builder()
            .customerCompanyId(command.customerCompanyId())
            .title(command.title())
            .amount(command.amount())
            .expectedCloseDate(command.expectedCloseDate())
            .build();

        Opportunity newOpportunity = Opportunity.create(createParams);
        Opportunity savedOpportunity = opportunityRepository.save(newOpportunity);
        
        log.info("Opportunity created successfully with ID: {}", savedOpportunity.getId().value());
        return savedOpportunity;
    }

    @Transactional
    public Opportunity handle(UpdateOpportunityDetailsCommand command) {
        log.info("Updating opportunity details: {}", command.opportunityId());
        
        Opportunity opportunity = findOpportunityOrThrow(command.opportunityId());
        
        if (!opportunity.canBeModified()) {
            throw new IllegalStateException("Cannot modify a closed opportunity");
        }
        
        opportunity.updateDetails(command.title(), command.amount(), command.expectedCloseDate());
        
        Opportunity updatedOpportunity = opportunityRepository.save(opportunity);
        log.info("Opportunity {} details updated successfully", command.opportunityId());
        
        return updatedOpportunity;
    }

    @Transactional
    public Opportunity handle(QualifyOpportunityCommand command) {
        return changeOpportunityStage(command.opportunityId(), "qualified", Opportunity::qualify);
    }

    @Transactional
    public Opportunity handle(MoveToProposalCommand command) {
        return changeOpportunityStage(command.opportunityId(), "proposal", Opportunity::moveToProposal);
    }

    @Transactional
    public Opportunity handle(MoveToNegotiationCommand command) {
        return changeOpportunityStage(command.opportunityId(), "negotiation", Opportunity::moveToNegotiation);
    }

    @Transactional
    public Opportunity handle(CloseOpportunityWonCommand command) {
        return changeOpportunityStage(command.opportunityId(), "closed won", Opportunity::closeWon);
    }

    @Transactional
    public Opportunity handle(CloseOpportunityLostCommand command) {
        return changeOpportunityStage(command.opportunityId(), "closed lost", Opportunity::closeLost);
    }

    @Transactional
    public Opportunity handle(ReopenOpportunityCommand command) {
        return changeOpportunityStage(command.opportunityId(), "reopened", Opportunity::reopen);
    }

    @Transactional
    public Opportunity handle(UpdateOpportunityAmountCommand command) {
        log.info("Updating amount for opportunity: {}", command.opportunityId());
        
        Opportunity opportunity = findOpportunityOrThrow(command.opportunityId());
        opportunity.updateAmount(command.amount());
        
        Opportunity updatedOpportunity = opportunityRepository.save(opportunity);
        log.info("Amount updated for opportunity {}", command.opportunityId());
        
        return updatedOpportunity;
    }

    @Transactional
    public void handle(DeleteOpportunityCommand command) {
        log.info("Deleting opportunity: {}", command.opportunityId());
        
        Opportunity opportunity = findOpportunityOrThrow(command.opportunityId());
        opportunity.markAsDeleted();
        
        opportunityRepository.delete(opportunity);
        log.info("Opportunity {} deleted successfully", command.opportunityId());
    }

    @Transactional(readOnly = true)
    public Opportunity handle(GetOpportunityByIdQuery query) {
        log.debug("Fetching opportunity by ID: {}", query.opportunityId());
        
        return opportunityRepository.findById(query.opportunityId())
            .orElseThrow(() -> new OpportunityNotFoundException(query.opportunityId().value()));
    }

    @Transactional(readOnly = true)
    public Page<Opportunity> handle(GetOpportunitiesByCustomerQuery query) {
        log.debug("Fetching opportunities for customer: {}", query.customerCompanyId());
        
        return opportunityRepository.findByCustomer(query.customerCompanyId(), query.pageable());
    }

    @Transactional(readOnly = true)
    public Page<Opportunity> handle(GetOpportunitiesByStageQuery query) {
        log.debug("Fetching opportunities by stage: {}", query.stage());
        
        return opportunityRepository.findByStage(query.stage(), query.pageable());
    }

    @Transactional(readOnly = true)
    public Page<Opportunity> handle(GetOpportunitiesByStagesQuery query) {
        log.debug("Fetching opportunities by stages: {}", query.stages());
        
        return opportunityRepository.findByStages(query.stages(), query.pageable());
    }

    @Transactional(readOnly = true)
    public Page<Opportunity> handle(GetActiveOpportunitiesQuery query) {
        log.debug("Fetching active opportunities");
        
        return opportunityRepository.findActiveOpportunities(query.pageable());
    }

    @Transactional(readOnly = true)
    public Page<Opportunity> handle(GetOverdueOpportunitiesQuery query) {
        log.debug("Fetching overdue opportunities");
        
        return opportunityRepository.findOverdueOpportunities(query.pageable());
    }

    @Transactional(readOnly = true)
    public Page<Opportunity> handle(GetWonOpportunitiesQuery query) {
        log.debug("Fetching won opportunities");
        
        return opportunityRepository.findWonOpportunities(query.pageable());
    }

    @Transactional(readOnly = true)
    public Page<Opportunity> handle(GetLostOpportunitiesQuery query) {
        log.debug("Fetching lost opportunities");
        
        return opportunityRepository.findLostOpportunities(query.pageable());
    }

    @Transactional(readOnly = true)
    public Page<Opportunity> handle(SearchOpportunitiesQuery query) {
        log.debug("Searching opportunities with criteria: {}", query);
        
        if (query.stages() != null && !query.stages().isEmpty()) {
            return opportunityRepository.findByStages(query.stages(), query.pageable());
        }
        
        if (query.customerCompanyId() != null) {
            return opportunityRepository.findByCustomer(query.customerCompanyId(), query.pageable());
        }
        
        return opportunityRepository.findActiveOpportunities(query.pageable());
    }

    @Transactional(readOnly = true)
    public OpportunityStatistics handle(GetOpportunityStatisticsQuery query) {
        log.debug("Fetching opportunity statistics for customer: {}", query.customerCompanyId());

        long totalOpportunities = opportunityRepository.findByCustomer(query.customerCompanyId()).size();
        long activeOpportunities = opportunityRepository.countActiveByCustomer(query.customerCompanyId());
        long wonOpportunities = opportunityRepository.countByCustomerAndStage(query.customerCompanyId(), OpportunityStage.CLOSED_WON);
        long lostOpportunities = opportunityRepository.countByCustomerAndStage(query.customerCompanyId(), OpportunityStage.CLOSED_LOST);
        double winRate = opportunityRepository.calculateWinRateByCustomer(query.customerCompanyId());
        
        return new OpportunityStatistics(
            totalOpportunities,
            activeOpportunities,
            wonOpportunities,
            lostOpportunities,
            winRate
        );
    }

    private Opportunity findOpportunityOrThrow(OpportunityId opportunityId) {
        return opportunityRepository.findById(opportunityId)
            .orElseThrow(() -> new OpportunityNotFoundException(opportunityId.value()));
    }

    private Opportunity changeOpportunityStage(OpportunityId opportunityId, String stageName, OpportunityStageChanger stageChanger) {
        log.info("Changing opportunity {} stage to {}", opportunityId, stageName);
        
        Opportunity opportunity = findOpportunityOrThrow(opportunityId);
        stageChanger.changeStage(opportunity);
        
        Opportunity updatedOpportunity = opportunityRepository.save(opportunity);
        log.info("Opportunity {} stage changed to {}", opportunityId, stageName);
        
        return updatedOpportunity;
    }

    private void validateExternalDependencies(CreateOpportunityCommand command) {
        try {
            externalValidator.validateCustomerExists(command.customerCompanyId());
        } catch (ExternalServiceException e) {
            log.error("External validation failed for opportunity creation: {}", e.getMessage());
            throw e;
        }
    }

    @FunctionalInterface
    private interface OpportunityStageChanger {
        void changeStage(Opportunity opportunity);
    }
    

    public record OpportunityStatistics(
        long totalOpportunities,
        long activeOpportunities,
        long wonOpportunities,
        long lostOpportunities,
        double winRate
    ) {}
}