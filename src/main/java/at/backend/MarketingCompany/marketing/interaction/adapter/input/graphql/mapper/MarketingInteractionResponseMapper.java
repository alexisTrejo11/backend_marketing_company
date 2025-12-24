package at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.mapper;

import at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.dto.MarketingInteractionOutput;
import at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.dto.InteractionStatisticsOutput;
import at.backend.MarketingCompany.marketing.interaction.core.application.dto.DeviceBreakdown;
import at.backend.MarketingCompany.marketing.interaction.core.application.dto.GeographicBreakdown;
import at.backend.MarketingCompany.marketing.interaction.core.application.dto.InteractionStatistics;
import at.backend.MarketingCompany.marketing.interaction.core.domain.entity.CampaignInteraction;
import at.backend.MarketingCompany.shared.PageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MarketingInteractionResponseMapper {

	private final ObjectMapper objectMapper;

	public MarketingInteractionOutput toResponse(CampaignInteraction domain) {
		if (domain == null) {
			return null;
		}

		return new MarketingInteractionOutput(
				domain.getId() != null ? domain.getId().getValue() : null,
				domain.getCampaignId() != null ? domain.getCampaignId().getValue() : null,
				null, // Campaign name would need to be fetched separately
				domain.getCustomerId() != null ? domain.getCustomerId().getValue() : null,
				null, // Customer name would need to be fetched separately
				domain.getMarketingInteractionType() != null ? domain.getMarketingInteractionType().name() : null,
				domain.getInteractionDate(),
				domain.getChannelId() != null ? domain.getChannelId().getValue() : null,
				null, // Channel name would need to be fetched separately
				domain.getUtmSource(),
				domain.getUtmMedium(),
				domain.getUtmCampaign(),
				domain.getUtmContent(),
				domain.getUtmTerm(),
				domain.getDeviceType(),
				domain.getDeviceOs(),
				domain.getBrowser(),
				domain.getCountryCode(),
				domain.getCity(),
				domain.getDealId(),
				domain.getConversionValue(),
				domain.isConversion(),
				domain.getLandingPageUrl(),
				domain.getReferrerUrl(),
				domain.getSessionId(),
				mapToJson(domain.getProperties()),
				domain.getCreatedAt()
		);
	}

	public PageResponse<MarketingInteractionOutput> toPageResponse(Page<CampaignInteraction> page) {
		return PageResponse.of(page.map(this::toResponse));
	}

	public InteractionStatisticsOutput toStatisticsResponse(InteractionStatistics domain) {
		if (domain == null) {
			return null;
		}

		return new InteractionStatisticsOutput(
				domain.campaignId(),
				domain.campaignName(),
				domain.totalInteractions(),
				domain.totalConversions(),
				domain.conversionRate(),
				domain.totalConversionValue(),
				domain.averageConversionValue(),
				domain.uniqueCustomers(),
				domain.uniqueChannels(),
				toTypeBreakdownResponse(domain.typeBreakdown()),
				toDeviceBreakdownResponse(domain.deviceBreakdown()),
				toGeographicBreakdownResponse(domain.geographicBreakdown())
		);
	}

	private InteractionStatisticsOutput.TypeBreakdownResponse toTypeBreakdownResponse(
			InteractionStatistics.InteractionTypeBreakdown domain) {

		if (domain == null) {
			return null;
		}

		return new InteractionStatisticsOutput.TypeBreakdownResponse(
				domain.adClicks(),
				domain.adViews(),
				domain.emailOpens(),
				domain.emailClicks(),
				domain.landingPageVisits(),
				domain.formSubmits(),
				domain.socialEngagements(),
				domain.webinarRegistrations(),
				domain.whitepaperDownloads()
		);
	}

	private InteractionStatisticsOutput.DeviceBreakdownResponse toDeviceBreakdownResponse(DeviceBreakdown domain) {

		if (domain == null) {
			return null;
		}

		return new InteractionStatisticsOutput.DeviceBreakdownResponse(
				domain.desktop(),
				domain.mobile(),
				domain.tablet(),
				domain.unknown()
		);
	}

	private InteractionStatisticsOutput.GeographicBreakdownResponse toGeographicBreakdownResponse(GeographicBreakdown domain) {
		if (domain == null) {
			return null;
		}

		List<InteractionStatisticsOutput.CountryCount> countries =
				domain.topCountries().entrySet().stream()
						.map(e -> new InteractionStatisticsOutput.CountryCount(e.getKey(), e.getValue()))
						.collect(Collectors.toList());

		List<InteractionStatisticsOutput.CityCount> cities =
				domain.topCities().entrySet().stream()
						.map(e -> new InteractionStatisticsOutput.CityCount(e.getKey(), e.getValue()))
						.collect(Collectors.toList());

		return new InteractionStatisticsOutput.GeographicBreakdownResponse(countries, cities);
	}

	private String mapToJson(java.util.Map<String, Object> map) {
		if (map == null || map.isEmpty()) {
			return null;
		}

		try {
			return objectMapper.writeValueAsString(map);
		} catch (Exception e) {
			return null;
		}
	}
}