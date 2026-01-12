package at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.controller;

import java.util.stream.Collectors;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import at.backend.MarketingCompany.config.ratelimit.base.GraphQLRateLimit;
import at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.OpportunityResponseMapper;
import at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.input.GetOpportunitiesByCustomerInput;
import at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.input.GetOpportunitiesByStageInput;
import at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.input.GetOpportunityStatisticsInput;
import at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.input.OpportunityFilterInput;
import at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.output.OpportunityOutput;
import at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.output.OpportunityStatisticsResponse;
import at.backend.MarketingCompany.crm.opportunity.core.application.queries.GetActiveOpportunitiesQuery;
import at.backend.MarketingCompany.crm.opportunity.core.application.queries.GetLostOpportunitiesQuery;
import at.backend.MarketingCompany.crm.opportunity.core.application.queries.GetOpportunityByIdQuery;
import at.backend.MarketingCompany.crm.opportunity.core.application.queries.GetOverdueOpportunitiesQuery;
import at.backend.MarketingCompany.crm.opportunity.core.application.queries.GetWonOpportunitiesQuery;
import at.backend.MarketingCompany.crm.opportunity.core.application.queries.SearchOpportunitiesQuery;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.Opportunity;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityStage;
import at.backend.MarketingCompany.crm.opportunity.core.port.input.OpportunityQueryService;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.shared.PageResponse;
import at.backend.MarketingCompany.shared.dto.PageInput;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OpportunityQueryController {
  private final OpportunityQueryService queryService;
  private final OpportunityResponseMapper opportunityResponseMapper;

  @QueryMapping
  @GraphQLRateLimit
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES')")
  public OpportunityOutput opportunity(@Argument @Valid @NotBlank String id) {
    var query = GetOpportunityByIdQuery.from(id);
    Opportunity opportunity = queryService.getOpportunityById(query);

    return opportunityResponseMapper.toOutput(opportunity);
  }

  @QueryMapping
  @GraphQLRateLimit
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES', 'ANALYST')")
  public PageResponse<OpportunityOutput> opportunities(
      @Argument @Valid @NotNull OpportunityFilterInput filter) {
    var searchQuery = createSearchQuery(filter);
    var opportunities = queryService.getOpportunities(searchQuery);

    return opportunityResponseMapper.toPageResponse(opportunities);
  }

  @QueryMapping
  @GraphQLRateLimit
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES')")
  public PageResponse<OpportunityOutput> opportunitiesByCustomer(
      @Argument @Valid @NotNull GetOpportunitiesByCustomerInput input) {
    var query = input.toQuery();
    var opportunities = queryService.getOpportunitiesByCustomer(query);

    return opportunityResponseMapper.toPageResponse(opportunities);
  }

  @QueryMapping
  @GraphQLRateLimit
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES')")
  public PageResponse<OpportunityOutput> opportunitiesByStage(
      @Argument @Valid @NotNull GetOpportunitiesByStageInput input) {
    var query = input.toQuery();
    var opportunities = queryService.getOpportunitiesByStage(query);

    return opportunityResponseMapper.toPageResponse(opportunities);
  }

  @QueryMapping
  @GraphQLRateLimit
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES')")
  public PageResponse<OpportunityOutput> activeOpportunities(@Argument @Valid @NotNull PageInput pageInput) {
    var query = new GetActiveOpportunitiesQuery(pageInput.toPageable());
    var opportunities = queryService.getActiveOpportunities(query);

    return opportunityResponseMapper.toPageResponse(opportunities);
  }

  @QueryMapping
  @GraphQLRateLimit
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES')")
  public PageResponse<OpportunityOutput> overdueOpportunities(@Argument @Valid @NotNull PageInput pageInput) {
    var query = new GetOverdueOpportunitiesQuery(pageInput.toPageable());
    var opportunities = queryService.getOverdueOpportunities(query);

    return opportunityResponseMapper.toPageResponse(opportunities);
  }

  @QueryMapping
  @GraphQLRateLimit
  public PageResponse<OpportunityOutput> wonOpportunities(@Argument @Valid @NotNull PageInput pageInput) {
    var query = new GetWonOpportunitiesQuery(pageInput.toPageable());
    var opportunities = queryService.getWonOpportunities(query);

    return opportunityResponseMapper.toPageResponse(opportunities);
  }

  @QueryMapping
  @GraphQLRateLimit
  public PageResponse<OpportunityOutput> lostOpportunities(@Argument @Valid @NotNull PageInput pageInput) {
    var query = new GetLostOpportunitiesQuery(pageInput.toPageable());
    var opportunities = queryService.getLostOpportunities(query);

    return opportunityResponseMapper.toPageResponse(opportunities);
  }

  @QueryMapping
  @GraphQLRateLimit
  public OpportunityStatisticsResponse opportunityStatistics(
      @Argument @Valid @NotNull GetOpportunityStatisticsInput input) {
    var query = input.toQuery();
    var statistics = queryService.getOpportunityStatistics(query);

    return opportunityResponseMapper.toStatisticsResponse(statistics);
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
