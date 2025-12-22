package at.backend.MarketingCompany.marketing.campaign.adapter.input.graphql.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateRevenueInput(
    @NotNull @Positive Long campaignId,
    @NotNull @Positive Double revenue
) {}