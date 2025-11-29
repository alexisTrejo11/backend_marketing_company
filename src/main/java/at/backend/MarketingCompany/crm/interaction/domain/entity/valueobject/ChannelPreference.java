package at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject;

import at.backend.MarketingCompany.crm.interaction.domain.exceptions.InteractionValidationException;

import java.util.Arrays;
import java.util.List;

public record ChannelPreference(String value) {
    private static final List<String> VALID_CHANNELS = Arrays.asList(
        "EMAIL", "PHONE", "IN_PERSON", "VIDEO_CALL", "CHAT", "SOCIAL_MEDIA", "SMS"
    );

    public ChannelPreference {
        if (value != null && !VALID_CHANNELS.contains(value.toUpperCase())) {
            throw new InteractionValidationException(
                "Invalid channel preference. Valid values are: " + VALID_CHANNELS
            );
        }
    }

    public static ChannelPreference from(String channel) {
        return channel != null ? new ChannelPreference(channel.toUpperCase()) : null;
    }
}
