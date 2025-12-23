package at.backend.MarketingCompany.marketing.channel.adapter.input.graphql.controller;

import at.backend.MarketingCompany.marketing.channel.adapter.input.graphql.dto.CreateChannelInput;
import at.backend.MarketingCompany.marketing.channel.adapter.input.graphql.dto.MarketingChannelOutput;
import at.backend.MarketingCompany.marketing.channel.adapter.input.graphql.dto.UpdateChannelRequest;
import at.backend.MarketingCompany.marketing.channel.core.application.command.CreateChannelCommand;
import at.backend.MarketingCompany.marketing.channel.core.application.command.UpdateChannelCommand;
import at.backend.MarketingCompany.marketing.channel.core.domain.entity.MarketingChannel;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.ChannelType;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.MarketingChannelId;
import at.backend.MarketingCompany.marketing.channel.core.port.input.ChannelServicePort;
import at.backend.MarketingCompany.shared.PageResponse;
import at.backend.MarketingCompany.shared.dto.PageInput;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MarketingChannelController {
	private final ChannelServicePort servicePort;

	@QueryMapping
	public MarketingChannelOutput channel(@Argument @Valid @NotBlank String id) {
		MarketingChannelId channelId = MarketingChannelId.of(id);
		MarketingChannel channel = servicePort.getChannelById(channelId);
		return MarketingChannelOutput.fromDomain(channel);
	}

	@QueryMapping
	public MarketingChannelOutput channelByName(
			@Argument @Valid @NotBlank @Length(min = 3, max = 255) String name) {
		name = name.trim();
		MarketingChannel channel = servicePort.getChannelByName(name);
		return MarketingChannelOutput.fromDomain(channel);
	}

	@QueryMapping
	public PageResponse<MarketingChannelOutput> activeChannels(@Argument @Valid @NotNull PageInput pageInput) {
		Pageable pageable = pageInput.toPageable();
		Page<MarketingChannel> page = servicePort.getActiveChannels(pageable);
		return PageResponse.of(page.map(MarketingChannelOutput::fromDomain));
	}

	@QueryMapping
	public PageResponse<MarketingChannelOutput> channelsByType(
			@Argument @Valid @NotNull ChannelType type,
			@Argument @Valid @NotNull PageInput pageInput) {
		Pageable pageable = pageInput.toPageable();
		Page<MarketingChannel> page = servicePort.getChannelsByType(type, pageable);
		return PageResponse.of(page.map(MarketingChannelOutput::fromDomain));
	}

	@MutationMapping
	public MarketingChannelOutput createChannel(@Argument @Valid @NotNull CreateChannelInput input) {
		CreateChannelCommand command = input.toCommand();
		MarketingChannel channel = servicePort.createChannel(command);
		return MarketingChannelOutput.fromDomain(channel);
	}

	@MutationMapping
	public MarketingChannelOutput updateChannel(@Argument @Valid @NotNull UpdateChannelRequest input) {
		UpdateChannelCommand command = input.toCommand();
		MarketingChannel channel = servicePort.updateChannel(command);
		return MarketingChannelOutput.fromDomain(channel);
	}

	@MutationMapping
	public MarketingChannelOutput activateChannel(@Argument @Valid @NotBlank String id) {
		MarketingChannelId channelId = MarketingChannelId.of(id);
		MarketingChannel channel = servicePort.activateChannel(channelId);
		return MarketingChannelOutput.fromDomain(channel);
	}

	@MutationMapping
	public MarketingChannelOutput deactivateChannel(@Argument @Valid @NotBlank String id) {
		MarketingChannelId channelId = MarketingChannelId.of(id);
		MarketingChannel channel = servicePort.deactivateChannel(channelId);
		return MarketingChannelOutput.fromDomain(channel);
	}
}
