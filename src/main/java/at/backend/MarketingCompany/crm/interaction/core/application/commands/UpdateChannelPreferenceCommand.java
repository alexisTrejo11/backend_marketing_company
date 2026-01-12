package at.backend.MarketingCompany.crm.interaction.core.application.commands;

import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.ChannelPreference;
import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.InteractionId;

public record UpdateChannelPreferenceCommand(
    InteractionId interactionId,
    ChannelPreference channelPreference) {
}
