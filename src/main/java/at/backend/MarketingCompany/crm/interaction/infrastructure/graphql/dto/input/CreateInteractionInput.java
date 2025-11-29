package at.backend.MarketingCompany.crm.interaction.infrastructure.graphql.dto.input;

import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.FeedbackType;
import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.InteractionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateInteractionInput(
    @NotBlank String customerId,
    @NotNull InteractionType type,
    @NotNull LocalDateTime dateTime,
    String description,
    @NotBlank String outcome,
    FeedbackType feedbackType,
    String channelPreference
) {}

