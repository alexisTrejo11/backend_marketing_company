package at.backend.MarketingCompany.crm.opportunity.infrastructure.graphql.dto;

public record OpportunityStatisticsResponse(
    long totalOpportunities,
    long activeOpportunities,
    long wonOpportunities,
    long lostOpportunities,
    double winRate
) {}
