package at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject;

import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;
import lombok.Builder;

@Builder
public record CreateInteractionParams(
    CustomerCompanyId customerCompanyId,
    InteractionType type,
    InteractionDateTime dateTime,
    InteractionDescription description,
    InteractionOutcome outcome,
    FeedbackType feedbackType,
    ChannelPreference channelPreference) {
}
