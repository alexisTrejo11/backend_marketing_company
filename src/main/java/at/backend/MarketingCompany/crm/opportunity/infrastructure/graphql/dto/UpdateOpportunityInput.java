package at.backend.MarketingCompany.crm.opportunity.infrastructure.graphql.dto;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateOpportunityInput(
    @NotBlank String opportunityId,
    @NotBlank String title,
    BigDecimal amount,
    LocalDate expectedCloseDate
) {}
