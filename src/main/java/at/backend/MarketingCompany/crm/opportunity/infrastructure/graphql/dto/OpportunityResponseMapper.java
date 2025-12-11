package at.backend.MarketingCompany.crm.opportunity.infrastructure.graphql.dto;

import at.backend.MarketingCompany.shared.PageResponse;
import at.backend.MarketingCompany.crm.interaction.infrastructure.graphql.dto.ExternalDataFetcher;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.Opportunity;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.ExpectedCloseDate;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.Amount;
import at.backend.MarketingCompany.crm.opportunity.infrastructure.graphql.dto.output.OpportunityResponse;
import at.backend.MarketingCompany.crm.opportunity.infrastructure.graphql.dto.output.OpportunityStatisticsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OpportunityResponseMapper {
    
    private final ExternalDataFetcher externalDataFetcher;

    public OpportunityResponse toResponse(Opportunity opportunity) {
        if (opportunity == null) return null;

        var customer = externalDataFetcher.fetchCustomerInfo(opportunity.getCustomerCompanyId());

        return new OpportunityResponse(
            opportunity.getId().value(),
            opportunity.getCustomerCompanyId().value(),
            customer,
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
            opportunity.getUpdatedAt()
        );
    }

    public List<OpportunityResponse> toGraphQLResponseList(List<Opportunity> opportunities) {
        return opportunities.stream()
            .map(this::toResponse)
            .toList();
    }

    public PageResponse<OpportunityResponse> toPageResponse(Page<Opportunity> opportunityPage) {
        var responsePage = opportunityPage.map(this::toResponse);
        return PageResponse.of(responsePage);
    }

    public OpportunityStatisticsResponse toStatisticsResponse(
            at.backend.MarketingCompany.crm.opportunity.application.OpportunityApplicationService.OpportunityStatistics statistics) {
        return new OpportunityStatisticsResponse(
            statistics.totalOpportunities(),
            statistics.activeOpportunities(),
            statistics.wonOpportunities(),
            statistics.lostOpportunities(),
            statistics.winRate()
        );
    }
}