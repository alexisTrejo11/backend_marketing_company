package at.backend.MarketingCompany.marketing.channel.adapter.output.persitence.mapper;

import at.backend.MarketingCompany.marketing.channel.adapter.output.persitence.model.MarketingChannelEntity;
import at.backend.MarketingCompany.marketing.channel.core.domain.entity.MarketingChannel;
import at.backend.MarketingCompany.marketing.channel.core.domain.entity.MarketingChannelReconstructParams;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.MarketingChannelId;
import org.springframework.stereotype.Component;

@Component
public class ChannelEntityMapper {

	public MarketingChannelEntity toEntity(MarketingChannel channel) {
		if (channel == null) {
			return null;
		}

		MarketingChannelEntity entity = new MarketingChannelEntity();
		entity.setId(channel.getId() != null ? channel.getId().getValue() : null);
		entity.setName(channel.getName());
		entity.setChannelType(channel.getChannelType() != null ? channel.getChannelType() : null);
		entity.setDescription(channel.getDescription());
		entity.setDefaultCostPerImpression(channel.getDefaultCostPerImpression());
		entity.setDefaultCostPerClick(channel.getDefaultCostPerClick());
		entity.setIsActive(channel.isActive());
		entity.setCreatedAt(channel.getCreatedAt());
		entity.setUpdatedAt(channel.getUpdatedAt());
		entity.setDeletedAt(channel.getDeletedAt());
		entity.setVersion(channel.getVersion());

		return entity;
	}

	public MarketingChannel toDomain(MarketingChannelEntity entity) {
		if (entity == null) {
			return null;
		}

		var params = MarketingChannelReconstructParams.builder()
			.id(entity.getId() != null ? new MarketingChannelId(entity.getId()) : null)
			.name(entity.getName())
			.channelType(entity.getChannelType())
			.description(entity.getDescription())
			.defaultCostPerClick(entity.getDefaultCostPerClick())
			.defaultCostPerImpression(entity.getDefaultCostPerImpression())
			.isActive(entity.getIsActive())
			.createdAt(entity.getCreatedAt())
			.updatedAt(entity.getUpdatedAt())
			.deletedAt(entity.getDeletedAt())
			.version(entity.getVersion())
			.build();

		return MarketingChannel.reconstruct(params);
	}
}
