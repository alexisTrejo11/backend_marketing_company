package at.backend.MarketingCompany.marketing.channel.adapter.input.graphql.dto;

import at.backend.MarketingCompany.marketing.channel.core.domain.entity.MarketingChannel;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.ChannelType;

import java.math.BigDecimal;

public record MarketingChannelOutput(
		 String id,
		 String name,
		 ChannelType channelType,
		 String description,
		 BigDecimal defaultCostPerClick,
		 BigDecimal defaultCostPerImpression,
		 boolean isActive,
		 String createdAt,
		 String updatedAt,
		 String deletedAt
) {

	public static MarketingChannelOutput fromDomain(MarketingChannel channel) {
		if (channel == null) {
			return null;
		}
		return new MarketingChannelOutput(
				channel.getId().getValue().toString(),
				channel.getName(),
				channel.getChannelType(),
				channel.getDescription(),
				channel.getDefaultCostPerClick(),
				channel.getDefaultCostPerImpression(),
				channel.isActive(),
				channel.getCreatedAt() != null ? channel.getCreatedAt().toString() : null,
				channel.getUpdatedAt() != null ? channel.getUpdatedAt().toString() : null,
				channel.getDeletedAt() != null ? channel.getDeletedAt().toString() : null
		);
	}
}

