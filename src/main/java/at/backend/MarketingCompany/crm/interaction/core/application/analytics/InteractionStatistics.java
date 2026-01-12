package at.backend.MarketingCompany.crm.interaction.core.application.analytics;

public record InteractionStatistics(
    long totalInteractions,
    long recentInteractions,
    long positiveInteractions,
    long negativeInteractions,
    long followUpRequired) {
}
