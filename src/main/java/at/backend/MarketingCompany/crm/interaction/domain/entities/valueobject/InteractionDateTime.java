package at.backend.MarketingCompany.crm.interaction.domain.entities.valueobject;

import at.backend.MarketingCompany.crm.interaction.domain.exceptions.InteractionValidationException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public record InteractionDateTime(LocalDateTime value) {
    public InteractionDateTime {
        if (value == null) {
            throw new InteractionValidationException("Interaction date time is required");
        }
        if (value.isAfter(LocalDateTime.now().plusHours(1))) {
            throw new InteractionValidationException("Interaction date time cannot be in the future");
        }
    }

    public boolean isRecent() {
        return isFromLastDays(1);
    }

    public boolean isFromLastDays(int days) {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(days);
        return value.isAfter(cutoff);
    }

    public boolean isFromLastHours(int hours) {
        LocalDateTime cutoff = LocalDateTime.now().minusHours(hours);
        return value.isAfter(cutoff);
    }

    public long daysSince() {
        return ChronoUnit.DAYS.between(value, LocalDateTime.now());
    }

    public static InteractionDateTime now() {
        return new InteractionDateTime(LocalDateTime.now());
    }

    public static InteractionDateTime from(LocalDateTime dateTime) {
        return dateTime != null ? new InteractionDateTime(dateTime) : null;
    }
}
