package at.backend.MarketingCompany.marketing.channel.adapter.input.graphql.dto;

import at.backend.MarketingCompany.marketing.channel.core.application.command.UpdateChannelCommand;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.ChannelType;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.MarketingChannelId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record UpdateChannelRequest(
		@NotNull(message = "Channel ID is required")
		@Positive(message = "Channel ID must be a positive number")
		Long id,

    @Size(max = 100, message = "Channel name cannot exceed 100 characters")
    String name,

		ChannelType channelType,

    String description,

    BigDecimal defaultCostPerClick,

    BigDecimal defaultCostPerImpression
) {

	public UpdateChannelCommand toCommand() {
		return new UpdateChannelCommand(
				new MarketingChannelId(id),
				this.name,
				this.channelType,
				this.description,
				this.defaultCostPerClick,
				this.defaultCostPerImpression
		);
	}
}