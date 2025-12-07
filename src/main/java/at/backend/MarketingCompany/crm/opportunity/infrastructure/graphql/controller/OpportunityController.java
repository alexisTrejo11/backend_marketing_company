package at.backend.MarketingCompany.crm.opportunity.infrastructure.graphql.controller;

import at.backend.MarketingCompany.common.PageResponse;
import at.backend.MarketingCompany.crm.opportunity.application.OpportunityApplicationService;
import at.backend.MarketingCompany.crm.opportunity.application.commands.*;
import at.backend.MarketingCompany.crm.opportunity.application.queries.*;
import at.backend.MarketingCompany.common.utils.PageInput;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityStage;
import at.backend.MarketingCompany.crm.opportunity.infrastructure.graphql.dto.*;
import at.backend.MarketingCompany.crm.opportunity.infrastructure.graphql.dto.input.*;
import at.backend.MarketingCompany.crm.opportunity.infrastructure.graphql.dto.output.OpportunityResponse;
import at.backend.MarketingCompany.crm.opportunity.infrastructure.graphql.dto.output.OpportunityStatisticsResponse;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerId;
import jakarta.validation.Valid;
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

  private final OpportunityApplicationService opportunityApplicationService;
  private final OpportunityResponseMapper opportunityResponseMapper;

  @QueryMapping
  public OpportunityResponse opportunity(@Argument String id) {
    log.debug("Fetching opportunity by ID: {}", id);

    var query = GetOpportunityByIdQuery.from(id);
    var opportunity = opportunityApplicationService.handle(query);

    return opportunityResponseMapper.toResponse(opportunity);
  }

  @QueryMapping
  public PageResponse<OpportunityResponse> opportunities(@Argument OpportunityFilterInput filter) {
    log.debug("Fetching opportunities with filter: {}", filter);

    var searchQuery = createSearchQuery(filter);
    var opportunities = opportunityApplicationService.handle(searchQuery);

    return opportunityResponseMapper.toPageResponse(opportunities);
  }

  @QueryMapping
  public PageResponse<OpportunityResponse> opportunitiesByCustomer(@Argument GetOpportunitiesByCustomerInput input) {
    log.debug("Fetching opportunities for customer: {}", input.customerId());

    var query = input.toQuery();
    var opportunities = opportunityApplicationService.handle(query);

    return opportunityResponseMapper.toPageResponse(opportunities);
  }

  @QueryMapping
  public PageResponse<OpportunityResponse> opportunitiesByStage(@Argument GetOpportunitiesByStageInput input) {
    log.debug("Fetching opportunities by stage: {}", input.stage());

    var query = input.toQuery();
    var opportunities = opportunityApplicationService.handle(query);

    return opportunityResponseMapper.toPageResponse(opportunities);
  }

  @QueryMapping
  public PageResponse<OpportunityResponse> activeOpportunities(@Argument PageInput pageInput) {
    log.debug("Fetching active opportunities");

    var query = new GetActiveOpportunitiesQuery(pageInput.toPageable());
    var opportunities = opportunityApplicationService.handle(query);

    return opportunityResponseMapper.toPageResponse(opportunities);
  }

  @QueryMapping
  public PageResponse<OpportunityResponse> overdueOpportunities(@Argument PageInput pageInput) {
    log.debug("Fetching overdue opportunities");

    var query = new GetOverdueOpportunitiesQuery(pageInput.toPageable());
    var opportunities = opportunityApplicationService.handle(query);

    return opportunityResponseMapper.toPageResponse(opportunities);
  }

  @QueryMapping
  public PageResponse<OpportunityResponse> wonOpportunities(@Argument PageInput pageInput) {
    log.debug("Fetching won opportunities");

    var query = new GetWonOpportunitiesQuery(pageInput.toPageable());
    var opportunities = opportunityApplicationService.handle(query);

    return opportunityResponseMapper.toPageResponse(opportunities);
  }

  @QueryMapping
  public PageResponse<OpportunityResponse> lostOpportunities(@Argument PageInput pageInput) {
    log.debug("Fetching lost opportunities");

    var query = new GetLostOpportunitiesQuery(pageInput.toPageable());
    var opportunities = opportunityApplicationService.handle(query);

    return opportunityResponseMapper.toPageResponse(opportunities);
  }

  @QueryMapping
  public OpportunityStatisticsResponse opportunityStatistics(@Argument GetOpportunityStatisticsInput input) {
    log.debug("Fetching opportunity statistics for customer: {}", input.customerId());

    var query = input.toQuery();
    var statistics = opportunityApplicationService.handle(query);

    return opportunityResponseMapper.toStatisticsResponse(statistics);
  }

  @MutationMapping
  public OpportunityResponse createOpportunity(@Valid @Argument CreateOpportunityInput input) {
    log.info("Creating new opportunity for customer: {}", input.customerId());

    var command = input.toCommand();

    var result = opportunityApplicationService.handle(command);
    return opportunityResponseMapper.toResponse(result);
  }

  @MutationMapping
  public OpportunityResponse updateOpportunity(@Valid @Argument UpdateOpportunityInput input) {
    log.info("Updating opportunity: {}", input.opportunityId());

    var command = input.toCommand();

    var result = opportunityApplicationService.handle(command);
    return opportunityResponseMapper.toResponse(result);
  }

  @MutationMapping
  public OpportunityResponse qualifyOpportunity(@Argument String opportunityId) {
    log.info("Qualifying opportunity: {}", opportunityId);

    var command = QualifyOpportunityCommand.from(opportunityId);
    var result = opportunityApplicationService.handle(command);

    return opportunityResponseMapper.toResponse(result);
  }

  @MutationMapping
  public OpportunityResponse moveToProposal(@Argument String opportunityId) {
    log.info("Moving opportunity to proposal: {}", opportunityId);

    var command = MoveToProposalCommand.from(opportunityId);
    var result = opportunityApplicationService.handle(command);

    return opportunityResponseMapper.toResponse(result);
  }

  @MutationMapping
  public OpportunityResponse moveToNegotiation(@Argument String opportunityId) {
    log.info("Moving opportunity to negotiation: {}", opportunityId);

    var command = MoveToNegotiationCommand.from(opportunityId);
    var result = opportunityApplicationService.handle(command);

    return opportunityResponseMapper.toResponse(result);
  }

  @MutationMapping
  public OpportunityResponse closeOpportunityWon(@Argument String opportunityId) {
    log.info("Closing opportunity as won: {}", opportunityId);

    var command = CloseOpportunityWonCommand.from(opportunityId);
    var result = opportunityApplicationService.handle(command);

    return opportunityResponseMapper.toResponse(result);
  }

  @MutationMapping
  public OpportunityResponse closeOpportunityLost(@Argument String opportunityId) {
    log.info("Closing opportunity as lost: {}", opportunityId);

    var command = CloseOpportunityLostCommand.from(opportunityId);
    var result = opportunityApplicationService.handle(command);

    return opportunityResponseMapper.toResponse(result);
  }

  @MutationMapping
  public OpportunityResponse reopenOpportunity(@Argument String opportunityId) {
    log.info("Reopening opportunity: {}", opportunityId);

    var command = ReopenOpportunityCommand.from(opportunityId);
    var result = opportunityApplicationService.handle(command);

    return opportunityResponseMapper.toResponse(result);
  }

  @MutationMapping
  public Boolean deleteOpportunity(@Argument String opportunityId) {
    log.info("Deleting opportunity: {}", opportunityId);

    var command = DeleteOpportunityCommand.from(opportunityId);
    opportunityApplicationService.handle(command);

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
        CustomerId.of(filter.customerId()),
        filter.pageInput().toPageable());
  }
}
