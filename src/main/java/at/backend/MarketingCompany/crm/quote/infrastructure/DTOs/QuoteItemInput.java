package at.backend.MarketingCompany.crm.quote.infrastructure.DTOs;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record QuoteItemInput(
        @NotNull(message = "Service Package ID is required.")
        String servicePackageId,

        @NotNull(message = "Discount Percentage is required.")
        @Positive(message = "Discount Percentage  must be positive.")
        BigDecimal discountPercentage
){}
