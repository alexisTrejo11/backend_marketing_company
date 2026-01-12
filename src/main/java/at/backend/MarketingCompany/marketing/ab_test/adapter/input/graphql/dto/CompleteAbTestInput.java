package at.backend.MarketingCompany.marketing.ab_test.adapter.input.graphql.dto;

import at.backend.MarketingCompany.marketing.ab_test.core.application.command.CompleteAbTestCommand;
import at.backend.MarketingCompany.marketing.ab_test.core.domain.valueobject.AbTestId;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CompleteAbTestInput(
    @NotNull @Positive
    Long testId,

    @NotBlank(message = "Winning variant is required")
    @Size(max = 100, message = "Winning variant cannot exceed 100 characters")
    String winningVariant,

    @NotNull(message = "Statistical significance is required")
    @DecimalMin(value = "0.0", message = "Statistical significance must be at least 0")
    @DecimalMax(value = "1.0", message = "Statistical significance cannot exceed 1")
    BigDecimal statisticalSignificance
) {

    public CompleteAbTestCommand toCommand() {
        return new CompleteAbTestCommand(
            new AbTestId(this.testId),
            this.winningVariant,
            this.statisticalSignificance
        );
    }
}