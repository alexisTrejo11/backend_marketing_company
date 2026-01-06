package at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.output;

import java.math.BigDecimal;

public record OpportunityStatisticsResponse(
    Long totalOpportunities,
    Long activeOpportunities,
    Long wonOpportunities,
    Long lostOpportunities,
    Double winRate,
    BigDecimal totalPipelineValue,
    BigDecimal averageDealSize,
    Long leadStageCount,
    Long QUALIFICATIONStageCount,
    Long proposalStageCount,
    Long negotiationStageCount) {
}
