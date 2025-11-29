package at.backend.MarketingCompany.crm.interaction.infrastructure.graphql.dto.output;

import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.FeedbackType;
import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.InteractionType;

import java.util.List;

public record CustomerInteractionAnalyticsResponse(
    String customerId,
    List<InteractionType> frequentInteractionTypes,
    FeedbackType predominantFeedback,
    long totalInteractions,
    double monthlyFrequency
) {}
