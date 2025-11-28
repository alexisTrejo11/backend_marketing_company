    package at.backend.MarketingCompany.crm.opportunity.infrastructure.graphql.controller;

import at.backend.MarketingCompany.crm.opportunity.application.OpportunityApplicationService;
import at.backend.MarketingCompany.crm.opportunity.application.commands.*;
import at.backend.MarketingCompany.crm.opportunity.application.queries.*;
import at.backend.MarketingCompany.common.utils.PageInput;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityStage;
import at.backend.MarketingCompany.crm.opportunity.infrastructure.graphql.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OpportunityController {
    
    private final OpportunityApplicationService opportunityApplicationService;
    private final OpportunityGraphQLMapper opportunityGraphQLMapper;


    @QueryMapping
    public OpportunityResponse opportunity(@Argument String id) {
        log.debug("Fetching opportunity by ID: {}", id);
        
        var query = new GetOpportunityByIdQuery(id);
        var opportunity = opportunityApplicationService.handle(query);
        
        return opportunityGraphQLMapper.toGraphQLResponse(opportunity);
    }

    @QueryMapping
    public List<OpportunityResponse> opportunities(@Argument PageInput pageInput, @Argument OpportunityFilterInput filter) {
        log.debug("Fetching opportunities with filter: {}", filter);
        
        var searchQuery = createSearchQuery(filter);
        var opportunities = opportunityApplicationService.handle(searchQuery);
        
        return opportunityGraphQLMapper.toGraphQLResponseList(opportunities);
    }

    @QueryMapping
    public List<OpportunityResponse> opportunitiesByCustomer(@Argument String customerId) {
        log.debug("Fetching opportunities for customer: {}", customerId);
        
        var query = new GetOpportunitiesByCustomerQuery(customerId);
        var opportunities = opportunityApplicationService.handle(query);
        
        return opportunityGraphQLMapper.toGraphQLResponseList(opportunities);
    }

    @QueryMapping
    public List<OpportunityResponse> opportunitiesByStage(@Argument String stage) {
        log.debug("Fetching opportunities by stage: {}", stage);
        
        var query = new GetOpportunitiesByStageQuery(
            OpportunityStage.valueOf(stage)
        );
        var opportunities = opportunityApplicationService.handle(query);
        
        return opportunityGraphQLMapper.toGraphQLResponseList(opportunities);
    }

    @QueryMapping
    public List<OpportunityResponse> activeOpportunities() {
        log.debug("Fetching active opportunities");
        
        var query = new GetActiveOpportunitiesQuery();
        var opportunities = opportunityApplicationService.handle(query);
        
        return opportunityGraphQLMapper.toGraphQLResponseList(opportunities);
    }

    @QueryMapping
    public List<OpportunityResponse> overdueOpportunities() {
        log.debug("Fetching overdue opportunities");
        
        var query = new GetOverdueOpportunitiesQuery();
        var opportunities = opportunityApplicationService.handle(query);
        
        return opportunityGraphQLMapper.toGraphQLResponseList(opportunities);
    }

    @QueryMapping
    public List<OpportunityResponse> wonOpportunities() {
        log.debug("Fetching won opportunities");
        
        var query = new GetWonOpportunitiesQuery();
        var opportunities = opportunityApplicationService.handle(query);
        
        return opportunityGraphQLMapper.toGraphQLResponseList(opportunities);
    }

    @QueryMapping
    public List<OpportunityResponse> lostOpportunities() {
        log.debug("Fetching lost opportunities");
        
        var query = new GetLostOpportunitiesQuery();
        var opportunities = opportunityApplicationService.handle(query);
        
        return opportunityGraphQLMapper.toGraphQLResponseList(opportunities);
    }

    @QueryMapping
    public OpportunityStatisticsResponse opportunityStatistics(@Argument String customerId) {
        log.debug("Fetching opportunity statistics for customer: {}", customerId);
        
        var query = new GetOpportunityStatisticsQuery(customerId);
        var statistics = opportunityApplicationService.handle(query);
        
        return opportunityGraphQLMapper.toStatisticsResponse(statistics);
    }

    // ===== MUTATIONS =====

    @MutationMapping
    public OpportunityResponse createOpportunity(@Valid @Argument CreateOpportunityInput input) {
        log.info("Creating new opportunity for customer: {}", input.customerId());
        
        var command = new CreateOpportunityCommand(
            new at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.external.CustomerId(input.customerId()),
            input.title(),
            at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityAmount.from(input.amount()),
            at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.ExpectedCloseDate.from(input.expectedCloseDate())
        );
        
        var result = opportunityApplicationService.handle(command);
        return opportunityGraphQLMapper.toGraphQLResponse(result);
    }

    @MutationMapping
    public OpportunityResponse updateOpportunity(@Valid @Argument UpdateOpportunityInput input) {
        log.info("Updating opportunity: {}", input.opportunityId());
        
        var command = new UpdateOpportunityDetailsCommand(
            input.opportunityId(),
            input.title(),
            at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityAmount.from(input.amount()),
            at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.ExpectedCloseDate.from(input.expectedCloseDate())
        );
        
        var result = opportunityApplicationService.handle(command);
        return opportunityGraphQLMapper.toGraphQLResponse(result);
    }

    @MutationMapping
    public OpportunityResponse qualifyOpportunity(@Argument String opportunityId) {
        log.info("Qualifying opportunity: {}", opportunityId);
        
        var command = new QualifyOpportunityCommand(opportunityId);
        var result = opportunityApplicationService.handle(command);
        
        return opportunityGraphQLMapper.toGraphQLResponse(result);
    }

    @MutationMapping
    public OpportunityResponse moveToProposal(@Argument String opportunityId) {
        log.info("Moving opportunity to proposal: {}", opportunityId);
        
        var command = new MoveToProposalCommand(opportunityId);
        var result = opportunityApplicationService.handle(command);
        
        return opportunityGraphQLMapper.toGraphQLResponse(result);
    }

    @MutationMapping
    public OpportunityResponse moveToNegotiation(@Argument String opportunityId) {
        log.info("Moving opportunity to negotiation: {}", opportunityId);
        
        var command = new MoveToNegotiationCommand(opportunityId);
        var result = opportunityApplicationService.handle(command);
        
        return opportunityGraphQLMapper.toGraphQLResponse(result);
    }

    @MutationMapping
    public OpportunityResponse closeOpportunityWon(@Argument String opportunityId) {
        log.info("Closing opportunity as won: {}", opportunityId);
        
        var command = new CloseOpportunityWonCommand(opportunityId);
        var result = opportunityApplicationService.handle(command);
        
        return opportunityGraphQLMapper.toGraphQLResponse(result);
    }

    @MutationMapping
    public OpportunityResponse closeOpportunityLost(@Argument String opportunityId) {
        log.info("Closing opportunity as lost: {}", opportunityId);
        
        var command = new CloseOpportunityLostCommand(opportunityId);
        var result = opportunityApplicationService.handle(command);
        
        return opportunityGraphQLMapper.toGraphQLResponse(result);
    }

    @MutationMapping
    public OpportunityResponse reopenOpportunity(@Argument String opportunityId) {
        log.info("Reopening opportunity: {}", opportunityId);
        
        var command = ReopenOpportunityCommand.from(opportunityId);
        var result = opportunityApplicationService.handle(command);
        
        return opportunityGraphQLMapper.toGraphQLResponse(result);
    }

    @MutationMapping
    public Boolean deleteOpportunity(@Argument String opportunityId) {
        log.info("Deleting opportunity: {}", opportunityId);
        
        var command = new DeleteOpportunityCommand(opportunityId);
        opportunityApplicationService.handle(command);
        
        return true;
    }

    // ===== PRIVATE METHODS =====

    private SearchOpportunitiesQuery createSearchQuery(OpportunityFilterInput filter) {
        if (filter == null) {
            return new SearchOpportunitiesQuery(null, null, null);
        }

        var stages = filter.stages() != null ? 
            filter.stages().stream()
                .map(Enum::valueOf)
                .toList() : null;

        return new SearchOpportunitiesQuery(
            filter.searchTerm(),
            stages,
            filter.customerId()
        );
    }
}