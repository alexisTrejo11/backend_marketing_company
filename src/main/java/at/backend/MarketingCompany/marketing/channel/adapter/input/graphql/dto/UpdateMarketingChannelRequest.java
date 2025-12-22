package at.backend.MarketingCompany.marketing.channel.adapter.input.graphql.dto;

import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record UpdateMarketingChannelRequest(
    @Size(max = 100, message = "Channel name cannot exceed 100 characters")
    String name,

    String description,

    BigDecimal defaultCostPerClick,

    BigDecimal defaultCostPerImpression,

    Boolean isActive
) {}