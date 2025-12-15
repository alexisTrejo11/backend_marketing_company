package at.backend.MarketingCompany.crm.interaction.core.application.analytics;

import java.util.List;

import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.FeedbackType;
import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.InteractionType;

public record CustomerInteractionAnalytics(
    String customerId,
    List<InteractionType> frequentInteractionTypes,
    FeedbackType predominantFeedback,
    long totalInteractions,
    double monthlyFrequency) {
}
