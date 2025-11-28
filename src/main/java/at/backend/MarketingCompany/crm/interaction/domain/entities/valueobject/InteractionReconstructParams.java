package at.backend.MarketingCompany.crm.interaction.domain.entities.valueobject;

import at.backend.MarketingCompany.customer.domain.ValueObjects.CustomerId;
import lombok.Builder;

import java.time.LocalDateTime;

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
    LocalDateTime updatedAt
) {}
