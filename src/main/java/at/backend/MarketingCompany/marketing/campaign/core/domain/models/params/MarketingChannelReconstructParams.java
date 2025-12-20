package at.backend.MarketingCompany.marketing.campaign.core.domain.models.params;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.ChannelType;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingChannelId;
import at.backend.MarketingCompany.marketing.core.domain.entity.MarketingChannel.ChannelType;

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
