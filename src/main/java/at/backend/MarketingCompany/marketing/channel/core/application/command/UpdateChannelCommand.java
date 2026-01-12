package at.backend.MarketingCompany.marketing.channel.core.application.command;

import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.ChannelType;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.MarketingChannelId;

import java.math.BigDecimal;

public record UpdateChannelCommand(
		MarketingChannelId id,
		String name,
		ChannelType channelType,
		String description,
		BigDecimal defaultCostPerClick,
		BigDecimal defaultCostPerImpression
) {
}
