package at.backend.MarketingCompany.crm.interaction.application.commands;


import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.*;
import at.backend.MarketingCompany.customer.domain.ValueObjects.CustomerId;

public record CreateInteractionCommand(
    CustomerId customerId,
    InteractionType type,
    InteractionDateTime dateTime,
    InteractionDescription description,
    InteractionOutcome outcome,
    FeedbackType feedbackType,
    ChannelPreference channelPreference
) {}

