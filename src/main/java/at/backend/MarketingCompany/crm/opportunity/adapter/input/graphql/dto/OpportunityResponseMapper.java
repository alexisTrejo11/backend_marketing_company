package at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto;

import at.backend.MarketingCompany.shared.PageResponse;
import at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.output.OpportunityResponse;
import at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.output.OpportunityStatisticsResponse;
import at.backend.MarketingCompany.crm.opportunity.core.application.OpportunityQueryServiceImpl.OpportunityStatistics;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.Opportunity;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.Amount;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.ExpectedCloseDate;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OpportunityResponseMapper {

  public OpportunityResponse toResponse(Opportunity opportunity) {
    if (opportunity == null)
      return null;

    return new OpportunityResponse(
        opportunity.getId() != null ? opportunity.getId().value() : null,
        opportunity.getCustomerCompanyId() != null ? opportunity.getCustomerCompanyId().value() : null,
        opportunity.getTitle(),
        opportunity.getAmount().map(Amount::value).orElse(null),
        opportunity.getStage(),
        opportunity.getExpectedCloseDate().map(ExpectedCloseDate::value).orElse(null),
        opportunity.isClosed(),
        opportunity.isWon(),
        opportunity.isLost(),
        opportunity.isOverdue(),
        opportunity.canBeModified(),
        opportunity.getCreatedAt(),
        opportunity.getUpdatedAt());
  }

  public List<OpportunityResponse> toGraphQLResponseList(List<Opportunity> opportunities) {
    if (opportunities == null)
      return List.of();

    return opportunities.stream()
        .map(this::toResponse)
        .toList();
  }

  public PageResponse<OpportunityResponse> toPageResponse(Page<Opportunity> opportunityPage) {
    if (opportunityPage == null)
      return PageResponse.empty();

    var responsePage = opportunityPage.map(this::toResponse);
    return PageResponse.of(responsePage);
  }

  public OpportunityStatisticsResponse toStatisticsResponse(
      OpportunityStatistics statistics) {
    if (statistics == null)
      return null;

    return new OpportunityStatisticsResponse(
        statistics.totalOpportunities(),
        statistics.activeOpportunities(),
        statistics.wonOpportunities(),
        statistics.lostOpportunities(),
        statistics.winRate());
  }
}
