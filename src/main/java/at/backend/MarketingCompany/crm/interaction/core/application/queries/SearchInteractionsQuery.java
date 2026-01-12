package at.backend.MarketingCompany.crm.interaction.core.application.queries;

import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.FeedbackType;
import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.InteractionType;

import java.time.LocalDateTime;

public record SearchInteractionsQuery(
    String searchTerm,
    String customerId,
    InteractionType type,
    FeedbackType feedbackType,
    LocalDateTime startDate,
    LocalDateTime endDate) {
}
