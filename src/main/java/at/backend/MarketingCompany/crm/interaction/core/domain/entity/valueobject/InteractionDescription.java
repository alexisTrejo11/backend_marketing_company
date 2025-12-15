package at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject;

import at.backend.MarketingCompany.crm.interaction.core.domain.exceptions.InteractionValidationException;

public record InteractionDescription(String value) {
  public InteractionDescription {
    if (value != null && value.length() > 2000) {
      throw new InteractionValidationException("Interaction description cannot exceed 2000 characters");
    }
  }

  public boolean hasContent() {
    return value != null && !value.trim().isEmpty();
  }

  public static InteractionDescription from(String description) {
    return description != null ? new InteractionDescription(description) : null;
  }
}
