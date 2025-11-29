package at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject;

import at.backend.MarketingCompany.crm.interaction.domain.exceptions.InteractionValidationException;

public record InteractionOutcome(String value) {
    public InteractionOutcome {
        if (value == null || value.isBlank()) {
            throw new InteractionValidationException("Interaction outcome is required");
        }
        if (value.length() > 500) {
            throw new InteractionValidationException("Interaction outcome cannot exceed 500 characters");
        }
    }

    public static InteractionOutcome from(String outcome) {
        return outcome != null ? new InteractionOutcome(outcome) : null;
    }
}
