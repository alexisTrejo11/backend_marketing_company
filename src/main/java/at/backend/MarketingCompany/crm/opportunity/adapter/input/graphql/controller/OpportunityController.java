package at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.controller;

import at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.OpportunityResponseMapper;
import at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.input.*;
import at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.output.OpportunityOutput;
import at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.output.OpportunityStatisticsResponse;
import at.backend.MarketingCompany.crm.opportunity.core.application.commands.*;
import at.backend.MarketingCompany.crm.opportunity.core.application.queries.*;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.Opportunity;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityStage;
import at.backend.MarketingCompany.crm.opportunity.core.port.input.OpportunityCommandService;
import at.backend.MarketingCompany.crm.opportunity.core.port.input.OpportunityQueryService;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.shared.PageResponse;
import at.backend.MarketingCompany.shared.dto.PageInput;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OpportunityController {
  private final OpportunityCommandService commandService;
  private final OpportunityQueryService queryService;
  private final OpportunityResponseMapper opportunityResponseMapper;

  @QueryMapping
  public OpportunityOutput opportunity(@Argument @Valid @NotBlank String id) {
    var query = GetOpportunityByIdQuery.from(id);
    Opportunity opportunity = queryService.getOpportunityById(query);

    return opportunityResponseMapper.toResponse(opportunity);
  }

  @QueryMapping
  public PageResponse<OpportunityOutput> opportunities(
      @Argument @Valid @NotNull OpportunityFilterInput filter
  ) {
    var searchQuery = createSearchQuery(filter);
    var opportunities = queryService.getOpportunities(searchQuery);

    return opportunityResponseMapper.toPageResponse(opportunities);
  }

  @QueryMapping
  public PageResponse<OpportunityOutput> opportunitiesByCustomer(
      @Argument @Valid @NotNull GetOpportunitiesByCustomerInput input
  ) {
    var query = input.toQuery();
    var opportunities = queryService.getOpportunitiesByCustomer(query);

    return opportunityResponseMapper.toPageResponse(opportunities);
  }

  @QueryMapping
  public PageResponse<OpportunityOutput> opportunitiesByStage(
      @Argument @Valid @NotNull GetOpportunitiesByStageInput input
  ) {
    var query = input.toQuery();
    var opportunities = queryService.getOpportunitiesByStage(query);

    return opportunityResponseMapper.toPageResponse(opportunities);
  }

  @QueryMapping
  public PageResponse<OpportunityOutput> activeOpportunities(@Argument @Valid @NotNull PageInput pageInput) {
    var query = new GetActiveOpportunitiesQuery(pageInput.toPageable());
    var opportunities = queryService.getActiveOpportunities(query);

    return opportunityResponseMapper.toPageResponse(opportunities);
  }

  @QueryMapping
  public PageResponse<OpportunityOutput> overdueOpportunities(@Argument @Valid @NotNull PageInput pageInput) {
    var query = new GetOverdueOpportunitiesQuery(pageInput.toPageable());
    var opportunities = queryService.getOverdueOpportunities(query);

    return opportunityResponseMapper.toPageResponse(opportunities);
  }

  @QueryMapping
  public PageResponse<OpportunityOutput> wonOpportunities(@Argument PageInput pageInput) {
    var query = new GetWonOpportunitiesQuery(pageInput.toPageable());
    var opportunities = queryService.getWonOpportunities(query);

    return opportunityResponseMapper.toPageResponse(opportunities);
  }

  @QueryMapping
  public PageResponse<OpportunityOutput> lostOpportunities(@Argument PageInput pageInput) {
    var query = new GetLostOpportunitiesQuery(pageInput.toPageable());
    var opportunities = queryService.getLostOpportunities(query);

    return opportunityResponseMapper.toPageResponse(opportunities);
  }

  @QueryMapping
  public OpportunityStatisticsResponse opportunityStatistics(@Argument GetOpportunityStatisticsInput input) {
    var query = input.toQuery();
    var statistics = queryService.getOpportunityStatistics(query);

    return opportunityResponseMapper.toStatisticsResponse(statistics);
  }

  @MutationMapping
  public OpportunityOutput createOpportunity(@Valid @Argument CreateOpportunityInput input) {
    log.info("Creating new opportunity for customer: {}", input.customerId());

    var command = input.toCommand();

    var result = commandService.createOpportunity(command);
    return opportunityResponseMapper.toResponse(result);
  }

  @MutationMapping
  public OpportunityOutput updateOpportunity(@Valid @Argument UpdateOpportunityInput input) {
    log.info("Updating opportunity: {}", input.opportunityId());

    var command = input.toCommand();

    var result = commandService.updateOpportunityDetails(command);
    return opportunityResponseMapper.toResponse(result);
  }

  @MutationMapping
  public OpportunityOutput qualifyOpportunity(@Argument String opportunityId) {
    log.info("Qualifying opportunity: {}", opportunityId);

    var command = QualifyOpportunityCommand.from(opportunityId);
    var result = commandService.qualifyOpportunity(command);

    return opportunityResponseMapper.toResponse(result);
  }

  @MutationMapping
  public OpportunityOutput moveToProposal(@Argument String opportunityId) {
    log.info("Moving opportunity to proposal: {}", opportunityId);

    var command = MoveToProposalCommand.from(opportunityId);
    var result = commandService.moveToProposal(command);

    return opportunityResponseMapper.toResponse(result);
  }

  @MutationMapping
  public OpportunityOutput moveToNegotiation(@Argument String opportunityId) {
    log.info("Moving opportunity to negotiation: {}", opportunityId);

    var command = MoveToNegotiationCommand.from(opportunityId);
    var result = commandService.moveToNegotiation(command);

    return opportunityResponseMapper.toResponse(result);
  }

  @MutationMapping
  public OpportunityOutput closeOpportunityWon(@Argument String opportunityId) {
    log.info("Closing opportunity as won: {}", opportunityId);

    var command = CloseOpportunityWonCommand.from(opportunityId);
    var result = commandService.closeOpportunityWon(command);

    return opportunityResponseMapper.toResponse(result);
  }

  @MutationMapping
  public OpportunityOutput closeOpportunityLost(@Argument String opportunityId) {
    log.info("Closing opportunity as lost: {}", opportunityId);

    var command = CloseOpportunityLostCommand.from(opportunityId);
    var result = commandService.closeOpportunityLost(command);

    return opportunityResponseMapper.toResponse(result);
  }

  @MutationMapping
  public OpportunityOutput reopenOpportunity(@Argument String opportunityId) {
    log.info("Reopening opportunity: {}", opportunityId);

    var command = ReopenOpportunityCommand.from(opportunityId);
    var result = commandService.reopenOpportunity(command);

    return opportunityResponseMapper.toResponse(result);
  }

  @MutationMapping
  public Boolean deleteOpportunity(@Argument String opportunityId) {
    log.info("Deleting opportunity: {}", opportunityId);

    var command = DeleteOpportunityCommand.from(opportunityId);
    commandService.deleteOpportunity(command);

    return true;
  }

  private SearchOpportunitiesQuery createSearchQuery(OpportunityFilterInput filter) {
    if (filter == null) {
      return SearchOpportunitiesQuery.empty();
    }

    var stages = filter.stages() != null ? filter.stages().stream()
        .map(OpportunityStage::valueOf)
        .collect(Collectors.toSet()) : null;

    return new SearchOpportunitiesQuery(
        filter.searchTerm(),
        stages,
        new CustomerCompanyId(filter.customerId()),
        filter.pageInput().toPageable());
  }
}
