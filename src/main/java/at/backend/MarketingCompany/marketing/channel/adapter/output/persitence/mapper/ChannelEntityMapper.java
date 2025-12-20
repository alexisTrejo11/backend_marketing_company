package at.backend.MarketingCompany.marketing.channel.adapter.output.persitence.mapper;

import at.backend.MarketingCompany.marketing.channel.adapter.output.persitence.model.MarketingChannelEntity;
import at.backend.MarketingCompany.marketing.channel.core.domain.entity.MarketingChannel;
import org.springframework.stereotype.Component;

@Component
public class ChannelEntityMapper {

	public MarketingChannelEntity toEntity(MarketingChannel channel) {
		if (channel == null) {
			return null;
		}

		// To be implemented: mapping logic from domain to entity
		return null;
	}

	public MarketingChannel toDomain(MarketingChannelEntity entity) {
		if (entity == null) {
			return null;
		}

		// To be implemented: mapping logic from entity to domain
		return null;
	}
}
