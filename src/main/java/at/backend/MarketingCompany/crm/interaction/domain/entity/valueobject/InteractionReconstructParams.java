package at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject;

import lombok.Builder;

import java.time.LocalDateTime;

import at.backend.MarketingCompany.customer.domain.valueobject.CustomerId;

@Builder
public record InteractionReconstructParams(
    InteractionId id,
    CustomerId customerId,
    InteractionType type,
    InteractionDateTime dateTime,
    InteractionDescription description,
    InteractionOutcome outcome,
    FeedbackType feedbackType,
    ChannelPreference channelPreference,
    Integer version,
    LocalDateTime deletedAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {
}
