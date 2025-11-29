package at.backend.MarketingCompany.crm.interaction.infrastructure.graphql.dto.output;

import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.FeedbackType;
import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.InteractionType;
import at.backend.MarketingCompany.crm.opportunity.infrastructure.graphql.dto.output.CustomerInfo;

import java.time.LocalDateTime;

public record InteractionResponse(
    String id,
    String customerId,
    CustomerInfo customer,
    InteractionType type,
    LocalDateTime dateTime,
    String description,
    String outcome,
    FeedbackType feedbackType,
    String channelPreference,
    boolean hasFeedback,
    boolean isPositiveFeedback,
    boolean isNegativeFeedback,
    boolean isRecent,
    boolean requiresFollowUp,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}

