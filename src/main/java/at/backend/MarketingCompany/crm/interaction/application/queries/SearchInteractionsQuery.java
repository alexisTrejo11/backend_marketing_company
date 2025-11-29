package at.backend.MarketingCompany.crm.interaction.application.queries;

import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.FeedbackType;
import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.InteractionType;

import java.time.LocalDateTime;

public record SearchInteractionsQuery(
    String searchTerm,
    String customerId,
    InteractionType type,
    FeedbackType feedbackType,
    LocalDateTime startDate,
    LocalDateTime endDate
) {}
