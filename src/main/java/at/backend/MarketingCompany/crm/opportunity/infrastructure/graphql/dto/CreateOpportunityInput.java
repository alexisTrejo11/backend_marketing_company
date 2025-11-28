package at.backend.MarketingCompany.crm.opportunity.infrastructure.graphql.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateOpportunityInput(
    @NotBlank String customerId,
    @NotBlank String title,
    BigDecimal amount,
    LocalDate expectedCloseDate
) {}

