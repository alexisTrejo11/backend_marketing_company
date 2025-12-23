package at.backend.MarketingCompany.marketing.channel.adapter.input.graphql.dto;

import at.backend.MarketingCompany.marketing.channel.core.application.command.CreateChannelCommand;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.ChannelType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateChannelInput(
    @NotBlank(message = "Channel name is required")
    @Size(min = 3, max = 100, message = "Channel name cannot exceed 100 characters")
    String name,

    @NotNull(message = "Channel type is required")
    ChannelType channelType,

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    String description,

    @Positive(message = "Cost per click must be positive")
    BigDecimal defaultCostPerClick,

    @Positive(message = "Cost per impression must be positive")
    BigDecimal defaultCostPerImpression
) {

	public CreateChannelCommand toCommand() {
		return new CreateChannelCommand(
				this.name,
				this.channelType,
				this.description,
				this.defaultCostPerClick,
				this.defaultCostPerImpression
		);
	}
}