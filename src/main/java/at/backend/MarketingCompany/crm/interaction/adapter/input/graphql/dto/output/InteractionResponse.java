package at.backend.MarketingCompany.crm.interaction.adapter.input.graphql.dto.output;

import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.FeedbackType;
import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.InteractionType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Builder
public record InteractionResponse(
    String id,
    String customerId,
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
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt) {
}
