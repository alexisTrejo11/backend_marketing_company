package at.backend.MarketingCompany.marketing.channel.core.application.command;

import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.ChannelType;

import java.math.BigDecimal;

public record CreateChannelCommand(
		String name,
		ChannelType channelType,
		String description,
		BigDecimal defaultCostPerClick,
		BigDecimal defaultCostPerImpression
) {
}
