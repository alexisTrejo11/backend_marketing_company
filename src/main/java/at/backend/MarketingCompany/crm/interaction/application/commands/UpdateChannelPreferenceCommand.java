package at.backend.MarketingCompany.crm.interaction.application.commands;

import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.ChannelPreference;
import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.InteractionId;

public record UpdateChannelPreferenceCommand(
    InteractionId interactionId,
    ChannelPreference channelPreference
) {}
