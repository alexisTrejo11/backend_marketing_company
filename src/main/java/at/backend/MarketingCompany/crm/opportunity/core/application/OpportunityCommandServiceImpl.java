package at.backend.MarketingCompany.crm.opportunity.core.application;

import at.backend.MarketingCompany.crm.deal.core.application.ExternalModuleValidator;
import at.backend.MarketingCompany.crm.opportunity.core.application.commands.*;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.Opportunity;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.*;
import at.backend.MarketingCompany.crm.opportunity.core.domain.exceptions.OpportunityNotFoundException;
import at.backend.MarketingCompany.crm.opportunity.core.port.input.OpportunityCommandService;
import at.backend.MarketingCompany.crm.opportunity.core.port.output.OpportunityRepository;
import at.backend.MarketingCompany.shared.domain.exceptions.ExternalServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpportunityCommandServiceImpl implements OpportunityCommandService {

  private final OpportunityRepository opportunityRepository;
  private final ExternalModuleValidator externalValidator;

  @Override
  @Transactional
  public Opportunity createOpportunity(CreateOpportunityCommand command) {
    log.info("Creating opportunity for customer: {}", command.customerCompanyId());

    validateExternalDependencies(command);

    var createParams = CreateOpportunityParams.builder()
        .customerCompanyId(command.customerCompanyId())
        .title(command.title())
        .amount(command.amount())
        .expectedCloseDate(command.expectedCloseDate())
        .build();

    Opportunity newOpportunity = Opportunity.create(createParams);
    Opportunity savedOpportunity = opportunityRepository.save(newOpportunity);

    log.info("Opportunity created successfully with ID: {}", savedOpportunity.getId());
    return savedOpportunity;
  }

  @Override
  @Transactional
  public Opportunity updateOpportunityDetails(UpdateOpportunityDetailsCommand command) {
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

  @Override
  @Transactional
  public Opportunity qualifyOpportunity(QualifyOpportunityCommand command) {
    return changeOpportunityStage(command.opportunityId(), "qualified", Opportunity::qualify);
  }

  @Override
  @Transactional
  public Opportunity moveToProposal(MoveToProposalCommand command) {
    return changeOpportunityStage(command.opportunityId(), "proposal", Opportunity::moveToProposal);
  }

  @Override
  @Transactional
  public Opportunity moveToNegotiation(MoveToNegotiationCommand command) {
    return changeOpportunityStage(command.opportunityId(), "negotiation", Opportunity::moveToNegotiation);
  }

  @Override
  @Transactional
  public Opportunity closeOpportunityWon(CloseOpportunityWonCommand command) {
    return changeOpportunityStage(command.opportunityId(), "closed won", Opportunity::closeWon);
  }

  @Override
  @Transactional
  public Opportunity closeOpportunityLost(CloseOpportunityLostCommand command) {
    return changeOpportunityStage(command.opportunityId(), "closed lost", Opportunity::closeLost);
  }

  @Override
  @Transactional
  public Opportunity reopenOpportunity(ReopenOpportunityCommand command) {
    return changeOpportunityStage(command.opportunityId(), "reopened", Opportunity::reopen);
  }

  @Override
  @Transactional
  public Opportunity updateOpportunityAmount(UpdateOpportunityAmountCommand command) {
    log.info("Updating amount for opportunity: {}", command.opportunityId());

    Opportunity opportunity = findOpportunityOrThrow(command.opportunityId());
    opportunity.updateAmount(command.amount());

    Opportunity updatedOpportunity = opportunityRepository.save(opportunity);
    log.info("Amount updated for opportunity {}", command.opportunityId());

    return updatedOpportunity;
  }

  @Override
  @Transactional
  public void deleteOpportunity(DeleteOpportunityCommand command) {
    log.info("Deleting opportunity: {}", command.opportunityId());

    Opportunity opportunity = findOpportunityOrThrow(command.opportunityId());
    opportunity.softDelete();

    opportunityRepository.delete(opportunity);
    log.info("Opportunity {} deleted successfully", command.opportunityId());
  }

  private Opportunity findOpportunityOrThrow(OpportunityId opportunityId) {
    return opportunityRepository.findById(opportunityId)
        .orElseThrow(() -> new OpportunityNotFoundException(opportunityId));
  }

  private Opportunity changeOpportunityStage(OpportunityId opportunityId, String stageName,
      OpportunityStageChanger stageChanger) {
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

}
