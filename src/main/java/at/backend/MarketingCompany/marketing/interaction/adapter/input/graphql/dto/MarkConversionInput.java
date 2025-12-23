package at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MarkConversionInput(
    @NotNull @Positive Long interactionId,
    @NotNull @Positive Long dealId,
    @NotNull @Positive Double conversionValue
) {}