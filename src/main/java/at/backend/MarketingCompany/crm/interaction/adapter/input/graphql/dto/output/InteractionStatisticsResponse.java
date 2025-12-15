package at.backend.MarketingCompany.crm.interaction.adapter.input.graphql.dto.output;

public record InteractionStatisticsResponse(
    long totalInteractions,
    long recentInteractions,
    long positiveInteractions,
    long negativeInteractions,
    long followUpRequired) {
}
