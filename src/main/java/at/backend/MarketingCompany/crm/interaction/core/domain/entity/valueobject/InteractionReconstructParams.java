package at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject;

import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record InteractionReconstructParams(
    InteractionId id,
    CustomerCompanyId customerCompanyId,
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
