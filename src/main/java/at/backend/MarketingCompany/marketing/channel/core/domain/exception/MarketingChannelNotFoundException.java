package at.backend.MarketingCompany.marketing.channel.core.domain.exception;

import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.MarketingChannelId;
import at.backend.MarketingCompany.shared.exception.NotFoundException;

public class MarketingChannelNotFoundException extends NotFoundException {
	public MarketingChannelNotFoundException(MarketingChannelId id) {
		super("Marketing Channel", id.asString());
	}

	public MarketingChannelNotFoundException(String name) {
		super("Marketing Channel", String.format("name['%s']", name));
	}
}
