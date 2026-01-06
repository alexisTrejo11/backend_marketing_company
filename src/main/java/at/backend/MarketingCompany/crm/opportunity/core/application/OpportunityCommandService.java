package at.backend.MarketingCompany.crm.opportunity.core.application;

import at.backend.MarketingCompany.crm.deal.core.application.ExternalModuleValidator;
import at.backend.MarketingCompany.crm.opportunity.core.application.commands.*;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.CreateOpportunityParams;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.Opportunity;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.*;
import at.backend.MarketingCompany.crm.opportunity.core.domain.exceptions.OpportunityNotFoundException;
import at.backend.MarketingCompany.crm.opportunity.core.port.input.OpportunityCommandInputPort;
import at.backend.MarketingCompany.crm.opportunity.core.port.output.OpportunityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpportunityCommandService implements OpportunityCommandInputPort {

  private final OpportunityRepository opportunityRepository;
  private final ExternalModuleValidator externalValidator;

  @Override
  @Transactional
  public Opportunity createOpportunity(CreateOpportunityCommand command) {
    log.info("Creating opportunity for customer: {}", command.customerCompanyId());

    externalValidator.validateCustomerExists(command.customerCompanyId());

    CreateOpportunityParams createParams = command.toCreateParams();
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

    opportunity.updateDetails(
        command.title(),
        command.amount(),
        command.expectedCloseDate(),
        command.nextSteps(),
        command.probability());

    Opportunity updatedOpportunity = opportunityRepository.save(opportunity);
    log.info("Opportunity {} details updated successfully", command.opportunityId());

    return updatedOpportunity;
  }

  @Override
  @Transactional
  public Opportunity qualifyOpportunity(QualifyOpportunityCommand command) {
    return changeOpportunityStage(
        command.opportunityId(),
        "QUALIFICATION",
        opportunity -> opportunity.qualify());
  }

  @Override
  @Transactional
  public Opportunity moveToProposal(MoveToProposalCommand command) {
    return changeOpportunityStage(
        command.opportunityId(),
        "proposal",
        opportunity -> opportunity.moveToProposal());
  }

  @Override
  @Transactional
  public Opportunity moveToNegotiation(MoveToNegotiationCommand command) {
    return changeOpportunityStage(
        command.opportunityId(),
        "negotiation",
        opportunity -> opportunity.moveToNegotiation());
  }

  @Override
  @Transactional
  public Opportunity closeOpportunityWon(CloseOpportunityWonCommand command) {
    log.info("Closing opportunity {} as WON", command.opportunityId());

    Opportunity opportunity = findOpportunityOrThrow(command.opportunityId());
    opportunity.closeWon(command.closeNotes());

    Opportunity updatedOpportunity = opportunityRepository.save(opportunity);
    log.info("Opportunity {} closed as WON", command.opportunityId());

    return updatedOpportunity;
  }

  @Override
  @Transactional
  public Opportunity closeOpportunityLost(CloseOpportunityLostCommand command) {
    log.info("Closing opportunity {} as LOST", command.opportunityId());

    Opportunity opportunity = findOpportunityOrThrow(command.opportunityId());
    opportunity.closeLost(command.lossReason(), command.notes());

    Opportunity updatedOpportunity = opportunityRepository.save(opportunity);
    log.info("Opportunity {} closed as LOST with reason: {}",
        command.opportunityId(), command.lossReason().value());

    return updatedOpportunity;
  }

  @Override
  @Transactional
  public Opportunity reopenOpportunity(ReopenOpportunityCommand command) {
    log.info("Reopening opportunity: {}", command.opportunityId());

    Opportunity opportunity = findOpportunityOrThrow(command.opportunityId());
    opportunity.reopen(command.reason());

    Opportunity updatedOpportunity = opportunityRepository.save(opportunity);
    log.info("Opportunity {} reopened successfully", command.opportunityId());

    return updatedOpportunity;
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
  public Opportunity updateOpportunityProbability(UpdateOpportunityProbabilityCommand command) {
    log.info("Updating probability for opportunity: {}", command.opportunityId());

    Opportunity opportunity = findOpportunityOrThrow(command.opportunityId());

    Probability probability = Probability.of(command.probability());
    opportunity.updateProbability(probability);

    Opportunity updatedOpportunity = opportunityRepository.save(opportunity);
    log.info("Probability updated to {}% for opportunity {}",
        command.probability(), command.opportunityId());

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

  private Opportunity changeOpportunityStage(
      OpportunityId opportunityId,
      String stageName,
      OpportunityStageChanger stageChanger) {
    log.info("Changing opportunity {} stage to {}", opportunityId, stageName);

    Opportunity opportunity = findOpportunityOrThrow(opportunityId);
    stageChanger.changeStage(opportunity);

    Opportunity updatedOpportunity = opportunityRepository.save(opportunity);
    log.info("Opportunity {} stage changed to {}", opportunityId, stageName);

    return updatedOpportunity;
  }

  @FunctionalInterface
  private interface OpportunityStageChanger {
    void changeStage(Opportunity opportunity);
  }
}
