package at.backend.MarketingCompany.marketing.channel.core.port.input;

import at.backend.MarketingCompany.marketing.channel.core.application.command.CreateChannelCommand;
import at.backend.MarketingCompany.marketing.channel.core.application.command.UpdateChannelCommand;
import at.backend.MarketingCompany.marketing.channel.core.domain.entity.MarketingChannel;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.ChannelType;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.MarketingChannelId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChannelServicePort {
	MarketingChannel getChannelById(MarketingChannelId id);

	MarketingChannel getChannelByName(String name);

	Page<MarketingChannel> getActiveChannels(Pageable pageable);

	Page<MarketingChannel> getChannelsByType(ChannelType type, Pageable pageable);

	Page<MarketingChannel> searchChannelsByName(String searchTerm, Pageable pageable);

	MarketingChannel createChannel(CreateChannelCommand command);

	MarketingChannel updateChannel(UpdateChannelCommand command);

	MarketingChannel activateChannel(MarketingChannelId id);

	MarketingChannel deactivateChannel(MarketingChannelId id);

	void deleteChannel(MarketingChannelId id);
}
