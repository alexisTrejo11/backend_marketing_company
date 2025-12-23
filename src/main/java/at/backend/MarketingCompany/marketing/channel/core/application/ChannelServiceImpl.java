package at.backend.MarketingCompany.marketing.channel.core.application;

import at.backend.MarketingCompany.marketing.channel.core.application.command.CreateChannelCommand;
import at.backend.MarketingCompany.marketing.channel.core.application.command.UpdateChannelCommand;
import at.backend.MarketingCompany.marketing.channel.core.domain.entity.MarketingChannel;
import at.backend.MarketingCompany.marketing.channel.core.domain.exception.MarketingChannelNotFoundException;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.ChannelType;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.MarketingChannelId;
import at.backend.MarketingCompany.marketing.channel.core.port.input.ChannelServicePort;
import at.backend.MarketingCompany.marketing.channel.core.port.output.ChannelRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelServicePort {
	private final ChannelRepositoryPort repositoryPort;

	@Override
	@Transactional(readOnly = true)
	public MarketingChannel getChannelById(MarketingChannelId id) {
		return getChannelByIdOrThrow(id);
	}

	@Override
	@Transactional(readOnly = true)
	public MarketingChannel getChannelByName(String name) {
		return repositoryPort.findByName(name)
				.orElseThrow(() -> new MarketingChannelNotFoundException(name));
	}

	@Override
	@Transactional(readOnly = true)
	public Page<MarketingChannel> getActiveChannels(Pageable pageable) {
		return repositoryPort.findByActiveStatus(true, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<MarketingChannel> getChannelsByType(ChannelType type, Pageable pageable) {
		return repositoryPort.findByChannelType(type, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<MarketingChannel> searchChannelsByName(String searchTerm, Pageable pageable) {
		return repositoryPort.searchByName(searchTerm, pageable);
	}

	@Override
	@Transactional
	public MarketingChannel createChannel(CreateChannelCommand command) {
		MarketingChannel channel = MarketingChannel.create(
				command.name(),
				command.channelType(),
				command.description(),
				command.defaultCostPerClick(),
				command.defaultCostPerImpression()
		);
		return repositoryPort.save(channel);
	}

	@Override
	@Transactional
	public MarketingChannel updateChannel(UpdateChannelCommand command) {
		MarketingChannel channel = getChannelByIdOrThrow(command.id());
		channel.updateDetails(
				command.name(),
				command.channelType(),
				command.description(),
				command.defaultCostPerClick(),
				command.defaultCostPerImpression()
		);
		return repositoryPort.save(channel);
	}


	@Override
	@Transactional
	public MarketingChannel activateChannel(MarketingChannelId id) {
		MarketingChannel channel = getChannelByIdOrThrow(id);
		channel.activate();
		return repositoryPort.save(channel);
	}

	@Override
	@Transactional
	public MarketingChannel deactivateChannel(MarketingChannelId id) {
		MarketingChannel channel = getChannelByIdOrThrow(id);
		channel.deactivate();
		return repositoryPort.save(channel);
	}

	@Transactional
	@Override
	public void deleteChannel(MarketingChannelId id) {
		MarketingChannel channel = getChannelByIdOrThrow(id);
		channel.softDelete();
		repositoryPort.save(channel);
	}

	private MarketingChannel getChannelByIdOrThrow(MarketingChannelId id) {
		return repositoryPort.findById(id)
				.orElseThrow(() -> new MarketingChannelNotFoundException(id));
	}
}
