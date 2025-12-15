package at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.output;

public record OpportunityStatisticsResponse(
    long totalOpportunities,
    long activeOpportunities,
    long wonOpportunities,
    long lostOpportunities,
    double winRate) {
}
