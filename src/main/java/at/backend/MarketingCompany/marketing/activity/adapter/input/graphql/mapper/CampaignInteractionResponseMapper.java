package at.backend.MarketingCompany.marketing.activity.adapter.input.graphql.mapper;

import at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.dto.CampaignInteractionOutput;
import at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.dto.InteractionStatisticsOutput;
import at.backend.MarketingCompany.marketing.interaction.core.application.dto.InteractionStatistics;
import at.backend.MarketingCompany.marketing.interaction.core.domain.entity.CampaignInteraction;
import at.backend.MarketingCompany.shared.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class CampaignInteractionResponseMapper {

	public CampaignInteractionOutput toOutput(CampaignInteraction interaction) {
		if (interaction == null) {
			return null;
		}

		return CampaignInteractionOutput.builder()
				.id(interaction.getId() != null ? interaction.getId().getValue() : null)
				.campaignId(interaction.getCampaignId() != null ? interaction.getCampaignId().getValue() : null)
				.customerId(interaction.getCustomerId() != null ? interaction.getCustomerId().getValue() : null)
				.interactionType(interaction.getMarketingInteractionType() != null ? interaction.getMarketingInteractionType().name() : null)
				.interactionDate(interaction.getInteractionDate())
				.channelId(interaction.getChannelId() != null ? interaction.getChannelId().getValue() : null)
				.utmSource(interaction.getUtmSource())
				.utmMedium(interaction.getUtmMedium())
				.utmCampaign(interaction.getUtmCampaign())
				.utmContent(interaction.getUtmContent())
				.utmTerm(interaction.getUtmTerm())
				.deviceType(interaction.getDeviceType())
				.deviceOs(interaction.getDeviceOs())
				.browser(interaction.getBrowser())
				.countryCode(interaction.getCountryCode())
				.city(interaction.getCity())
				.dealId(interaction.getDealId() != null ? interaction.getDealId() : null)
				.conversionValue(interaction.getConversionValue())
				.isConversion(interaction.isConversion())
				.landingPageUrl(interaction.getLandingPageUrl())
				.referrerUrl(interaction.getReferrerUrl())
				.sessionId(interaction.getSessionId())
				.properties(interaction.getProperties())
				.createdAt(interaction.getCreatedAt())
				.build();
	}

	public PageResponse<CampaignInteractionOutput> toOutputPage(Page<CampaignInteraction> interactionPage) {
		if (interactionPage == null) {
			return PageResponse.empty();
		}

		return PageResponse.of(interactionPage.map(this::toOutput));
	}

	public InteractionStatisticsOutput toStatisticsResponse(InteractionStatistics statistics) {
		if (statistics == null) {
			return null;
		}

		// TODO: Map missing fields if needed
		return InteractionStatisticsOutput.builder()
				.totalInteractions(statistics.totalInteractions())
				.totalConversions(statistics.totalConversions())
				.totalConversionValue(statistics.totalConversionValue())
				.uniqueCustomers(statistics.uniqueCustomers())
				.uniqueChannels(statistics.uniqueChannels())
				.build();
	}
}
