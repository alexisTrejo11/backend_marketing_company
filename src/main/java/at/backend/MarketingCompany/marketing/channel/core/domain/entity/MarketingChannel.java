package at.backend.MarketingCompany.marketing.channel.core.domain.entity;

import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.MarketingChannelId;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.ChannelType;
import at.backend.MarketingCompany.shared.domain.BaseDomainEntity;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class MarketingChannel extends BaseDomainEntity<MarketingChannelId> {
  private String name;
  private ChannelType channelType;
  private String description;
  private BigDecimal defaultCostPerClick;
  private BigDecimal defaultCostPerImpression;
  private boolean isActive;

  private MarketingChannel() {
    this.isActive = true;
  }

  public MarketingChannel(MarketingChannelId id) {
    super(id);
    this.isActive = true;
  }

  public static MarketingChannel create(
      String name,
      ChannelType channelType,
      String description,
      BigDecimal defaultCostPerClick,
			BigDecimal defaultCostPerImpression) {
    
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Channel name is required");
    }
    if (channelType == null) {
      throw new IllegalArgumentException("Channel type is required");
    }

    MarketingChannel channel = new MarketingChannel(MarketingChannelId.generate());
    channel.name = name;
    channel.channelType = channelType;
    channel.description = description;
    channel.isActive = true;
		channel.defaultCostPerClick = defaultCostPerClick;
		channel.defaultCostPerImpression = defaultCostPerImpression;

    return channel;
  }

	public void updateDetails(
			String name,
			ChannelType channelType,
			String description,
			BigDecimal defaultCostPerClick,
			BigDecimal defaultCostPerImpression) {
		if (name != null) {
			if (name.isBlank()) {
				throw new IllegalArgumentException("Channel name cannot be blank");
			}
			this.name = name;
		}

		if (channelType != null) {
			this.channelType = channelType;
		}

		if (description != null) {
			this.description = description;
		}

		if (defaultCostPerClick != null) {
			this.defaultCostPerClick = defaultCostPerClick;
		}

		if (defaultCostPerImpression != null) {
			this.defaultCostPerImpression = defaultCostPerImpression;
		}
	}

  public static MarketingChannel reconstruct(MarketingChannelReconstructParams params) {
    if (params == null) {
      return null;
    }

    MarketingChannel channel = new MarketingChannel();
    channel.id = params.id();
    channel.name = params.name();
    channel.channelType = params.channelType();
    channel.description = params.description();
    channel.defaultCostPerClick = params.defaultCostPerClick();
    channel.defaultCostPerImpression = params.defaultCostPerImpression();
    channel.isActive = params.isActive() != null ? params.isActive() : true;
    channel.createdAt = params.createdAt();
    channel.updatedAt = params.updatedAt();
    channel.deletedAt = params.deletedAt();
    channel.version = params.version();

    return channel;
  }

  public void deactivate() {
    this.isActive = false;
  }

  public void activate() {
    this.isActive = true;
  }

}
