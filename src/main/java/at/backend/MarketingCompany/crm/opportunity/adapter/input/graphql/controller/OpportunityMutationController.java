package at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.controller;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import at.backend.MarketingCompany.config.ratelimit.base.GraphQLRateLimit;
import at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.OpportunityResponseMapper;
import at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.input.CloseOpportunityLostInput;
import at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.input.CloseOpportunityWonInput;
import at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.input.CreateOpportunityInput;
import at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.input.ReopenOpportunityInput;
import at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.input.UpdateOpportunityDetailsInput;
import at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.input.UpdateOpportunityProbabilityInput;
import at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.output.OpportunityOutput;
import at.backend.MarketingCompany.crm.opportunity.core.application.commands.DeleteOpportunityCommand;
import at.backend.MarketingCompany.crm.opportunity.core.application.commands.MoveToNegotiationCommand;
import at.backend.MarketingCompany.crm.opportunity.core.application.commands.MoveToProposalCommand;
import at.backend.MarketingCompany.crm.opportunity.core.application.commands.QualifyOpportunityCommand;
import at.backend.MarketingCompany.crm.opportunity.core.port.input.OpportunityCommandInputPort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OpportunityMutationController {
  private final OpportunityCommandInputPort inputPort;
  private final OpportunityResponseMapper opportunityResponseMapper;

  @MutationMapping
  @GraphQLRateLimit("resource-mutation")
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES')")
  public OpportunityOutput createOpportunity(@Valid @Argument CreateOpportunityInput input) {
    log.info("Creating new opportunity for customer: {}", input.customerId());

    var command = input.toCommand();
    var result = inputPort.createOpportunity(command);

    return opportunityResponseMapper.toOutput(result);
  }

  @MutationMapping
  @GraphQLRateLimit("resource-mutation")
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES')")
  public OpportunityOutput updateOpportunityDetails(@Valid @Argument UpdateOpportunityDetailsInput input) {
    log.info("Updating opportunity details: {}", input.opportunityId());

    var command = input.toCommand();
    var result = inputPort.updateOpportunityDetails(command);

    return opportunityResponseMapper.toOutput(result);
  }

  @MutationMapping
  @GraphQLRateLimit("resource-mutation")
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES')")
  public OpportunityOutput qualifyOpportunity(@Argument String opportunityId) {
    log.info("Qualifying opportunity: {}", opportunityId);

    var command = QualifyOpportunityCommand.from(opportunityId);
    var result = inputPort.qualifyOpportunity(command);

    return opportunityResponseMapper.toOutput(result);
  }

  @MutationMapping
  @GraphQLRateLimit("resource-mutation")
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES')")
  public OpportunityOutput moveToProposal(@Argument String opportunityId) {
    log.info("Moving opportunity to proposal: {}", opportunityId);

    var command = MoveToProposalCommand.from(opportunityId);
    var result = inputPort.moveToProposal(command);

    return opportunityResponseMapper.toOutput(result);
  }

  @MutationMapping
  @GraphQLRateLimit("resource-mutation")
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES')")
  public OpportunityOutput moveToNegotiation(@Argument String opportunityId) {
    log.info("Moving opportunity to negotiation: {}", opportunityId);

    var command = MoveToNegotiationCommand.from(opportunityId);
    var result = inputPort.moveToNegotiation(command);

    return opportunityResponseMapper.toOutput(result);
  }

  @MutationMapping
  @GraphQLRateLimit("resource-mutation")
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES')")
  public OpportunityOutput closeOpportunityWon(@Argument CloseOpportunityWonInput input) {
    log.info("Closing opportunity as won: {}", input.opportunityId());

    var command = input.toCommand();
    var result = inputPort.closeOpportunityWon(command);

    return opportunityResponseMapper.toOutput(result);
  }

  @MutationMapping
  @GraphQLRateLimit("resource-mutation")
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES')")
  public OpportunityOutput closeOpportunityLost(@Argument CloseOpportunityLostInput input) {
    log.info("Closing opportunity as lost: {}", input.opportunityId());

    var command = input.toCommand();
    var result = inputPort.closeOpportunityLost(command);

    return opportunityResponseMapper.toOutput(result);
  }

  @MutationMapping
  @GraphQLRateLimit("resource-mutation")
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES')")
  public OpportunityOutput reopenOpportunity(@Argument ReopenOpportunityInput input) {
    log.info("Reopening opportunity: {}", input.opportunityId());

    var command = input.toCommand();
    var result = inputPort.reopenOpportunity(command);

    return opportunityResponseMapper.toOutput(result);
  }

  @MutationMapping
  @GraphQLRateLimit("resource-mutation")
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES')")
  public OpportunityOutput updateOpportunityProbability(@Valid @Argument UpdateOpportunityProbabilityInput input) {
    log.info("Updating probability for opportunity: {}", input.opportunityId());

    var command = input.toCommand();
    var result = inputPort.updateOpportunityProbability(command);

    return opportunityResponseMapper.toOutput(result);
  }

  @MutationMapping
  @GraphQLRateLimit("resource-mutation")
  @PreAuthorize("hasRole('ADMIN')")
  public Boolean deleteOpportunity(@Argument String opportunityId) {
    log.info("Deleting opportunity: {}", opportunityId);

    var command = DeleteOpportunityCommand.from(opportunityId);
    inputPort.deleteOpportunity(command);

    return true;
  }
}
