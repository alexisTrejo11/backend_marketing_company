package at.backend.MarketingCompany.crm.interaction.infrastructure.graphql.dto.output;

public record InteractionStatisticsResponse(
    long totalInteractions,
    long recentInteractions,
    long positiveInteractions,
    long negativeInteractions,
    long followUpRequired
) {}
