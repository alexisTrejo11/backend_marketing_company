package at.backend.MarketingCompany.crm.interaction.infrastructure.graphql.dto.input;

import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.FeedbackType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddFeedbackInput(
    @NotBlank String interactionId,
    @NotNull FeedbackType feedbackType,
    String notes
) {}
