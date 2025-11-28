package at.backend.MarketingCompany.crm.opportunity.infrastructure.graphql.dto;

import at.backend.MarketingCompany.crm.opportunity.domain.entity.Opportunity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OpportunityGraphQLMapper {
    
    private final ExternalDataFetcher externalDataFetcher;

    public OpportunityResponse toGraphQLResponse(Opportunity opportunity) {
        if (opportunity == null) return null;

        var customer = externalDataFetcher.fetchCustomerInfo(opportunity.getCustomerId().value());

        return new OpportunityResponse(
            opportunity.getId().value(),
            opportunity.getCustomerId().value(),
            customer,
            opportunity.getTitle(),
            opportunity.getAmount().map(amount -> amount.value()).orElse(null),
            opportunity.getStage(),
            opportunity.getExpectedCloseDate().map(closeDate -> closeDate.value()).orElse(null),
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
            .map(this::toGraphQLResponse)
            .toList();
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