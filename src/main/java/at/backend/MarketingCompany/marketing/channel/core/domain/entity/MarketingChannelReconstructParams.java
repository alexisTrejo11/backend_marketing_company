package at.backend.MarketingCompany.marketing.channel.core.domain.entity;

import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.ChannelType;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.MarketingChannelId;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MarketingChannelReconstructParams(
    MarketingChannelId id,
    String name,
    ChannelType channelType,
    String description,
    BigDecimal defaultCostPerClick,
    BigDecimal defaultCostPerImpression,
    Boolean isActive,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime deletedAt,
    Integer version
) {}
