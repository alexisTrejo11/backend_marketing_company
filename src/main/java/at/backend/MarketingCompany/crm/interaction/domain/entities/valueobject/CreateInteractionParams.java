package at.backend.MarketingCompany.crm.interaction.domain.entities.valueobject;

import at.backend.MarketingCompany.customer.domain.ValueObjects.CustomerId;
import lombok.Builder;

@Builder
public record CreateInteractionParams(
    CustomerId customerId,
    InteractionType type,
    InteractionDateTime dateTime,
    InteractionDescription description,
    InteractionOutcome outcome,
    FeedbackType feedbackType,
    ChannelPreference channelPreference
) {}
