package at.backend.MarketingCompany.marketing.campaign.adapter.input.graphql.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddSpendingInput(
    @NotNull @Positive Long campaignId,
    @NotNull @Positive Double amount,
    String description
) {}