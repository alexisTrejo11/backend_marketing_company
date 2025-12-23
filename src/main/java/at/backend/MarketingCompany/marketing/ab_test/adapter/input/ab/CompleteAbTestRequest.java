package at.backend.MarketingCompany.marketing.ab_test.adapter.input.ab;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CompleteAbTestRequest(
    @NotBlank(message = "Winning variant is required")
    @Size(max = 100, message = "Winning variant cannot exceed 100 characters")
    String winningVariant,

    @NotNull(message = "Statistical significance is required")
    @DecimalMin(value = "0.0", message = "Statistical significance must be at least 0")
    @DecimalMax(value = "1.0", message = "Statistical significance cannot exceed 1")
    BigDecimal statisticalSignificance
) {}