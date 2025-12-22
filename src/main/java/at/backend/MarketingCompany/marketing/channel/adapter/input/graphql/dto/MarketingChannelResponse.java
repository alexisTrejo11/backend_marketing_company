package at.backend.MarketingCompany.marketing.channel.adapter.input.graphql.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record MarketingChannelResponse(
    Long id,
    String name,
    String channelType,
    String description,
    BigDecimal defaultCostPerClick,
    BigDecimal defaultCostPerImpression,
    Boolean isActive,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {}
