package at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record MarkInteractionAsConversionRequest(
		@NotNull(message = "Deal ID is required")
    @Positive(message = "Deal ID must be positive")
    Long dealId,

		@NotNull(message = "Conversion value is required")
    @DecimalMin(value = "0.01", message = "Conversion value must be greater than 0")
		BigDecimal conversionValue
) {}