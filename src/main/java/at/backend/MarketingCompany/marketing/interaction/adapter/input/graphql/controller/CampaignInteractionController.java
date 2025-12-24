package at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.controller;

import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.DealId;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.MarketingChannelId;
import at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.dto.*;
import at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.mapper.MarketingInteractionResponseMapper;
import at.backend.MarketingCompany.marketing.interaction.core.application.command.*;
import at.backend.MarketingCompany.marketing.interaction.core.application.dto.InteractionStatistics;
import at.backend.MarketingCompany.marketing.interaction.core.application.query.InteractionQuery;
import at.backend.MarketingCompany.marketing.interaction.core.domain.entity.CampaignInteraction;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.CampaignInteractionId;
import at.backend.MarketingCompany.marketing.interaction.core.port.input.CampaignInteractionServicePort;
import at.backend.MarketingCompany.shared.PageResponse;
import at.backend.MarketingCompany.shared.dto.PageInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CampaignInteractionController {

	private final CampaignInteractionServicePort interactionService;
	private final MarketingInteractionResponseMapper interactionMapper;
	private final ObjectMapper objectMapper;


	@QueryMapping
	public MarketingInteractionOutput marketingInteraction(@Argument @NotNull @Positive Long id) {
		log.debug("GraphQL Query: interaction with id: {}", id);

		var interactionId = new CampaignInteractionId(id);
		CampaignInteraction interaction = interactionService.getInteractionById(interactionId);

		return interactionMapper.toResponse(interaction);
	}

	@QueryMapping
	public PageResponse<MarketingInteractionOutput> searchMarketingInteractions(
			@Argument InteractionFilterInput filter,
			@Argument PageInput pageInput) {

		log.debug("GraphQL Query: searchInteractions with filter: {}", filter);

		InteractionQuery query = buildInteractionQuery(filter);
		Page<CampaignInteraction> interactionPage = interactionService.searchInteractions(
				query,
				pageInput.toPageable()
		);

		return interactionMapper.toPageResponse(interactionPage);
	}

	@QueryMapping
	public PageResponse<MarketingInteractionOutput> marketingInteractionsByCampaign(
			@Argument @NotNull @Positive Long campaignId,
			@Argument PageInput pageInput) {

		log.debug("GraphQL Query: interactionsByCampaign with campaignId: {}", campaignId);

		Page<CampaignInteraction> interactionPage = interactionService.getInteractionsByCampaign(
				 new MarketingCampaignId(campaignId),
				pageInput.toPageable()
		);

		return interactionMapper.toPageResponse(interactionPage);
	}

	@QueryMapping
	public PageResponse<MarketingInteractionOutput> marketingInteractionsByCustomer(
			@Argument @NotNull @Positive Long customerId,
			@Argument PageInput pageInput) {
		log.debug("GraphQL Query: interactionsByCustomer with customerId: {}", customerId);

		Page<CampaignInteraction> interactionPage = interactionService.getInteractionsByCustomer(
				new CustomerCompanyId(customerId),
				pageInput.toPageable()
		);

		return interactionMapper.toPageResponse(interactionPage);
	}

	@QueryMapping
	public PageResponse<MarketingInteractionOutput> conversionsByCampaign(
			@Argument @NotNull @Positive Long campaignId,
			@Argument PageInput pageInput) {

		log.debug("GraphQL Query: conversionsByCampaign with campaignId: {}", campaignId);

		var campaignIdVO = new MarketingCampaignId(campaignId);
		Page<CampaignInteraction> interactionPage = interactionService.getConversionsByCampaign(
				campaignIdVO,
				pageInput.toPageable()
		);

		return interactionMapper.toPageResponse(interactionPage);
	}

	@QueryMapping
	public PageResponse<MarketingInteractionOutput> interactionsByChannel(
			@Argument @NotNull @Positive Long channelId,
			@Argument PageInput pageInput) {
		log.debug("GraphQL Query: interactionsByChannel with channelId: {}", channelId);

		Page<CampaignInteraction> interactionPage = interactionService.getInteractionsByChannel(
				new MarketingChannelId(channelId),
				pageInput.toPageable()
		);

		return interactionMapper.toPageResponse(interactionPage);
	}

	@QueryMapping
	public PageResponse<MarketingInteractionOutput> marketingInteractionsByDateRange(
			@Argument @NotBlank String startDate,
			@Argument @NotBlank String endDate,
			@Argument PageInput pageInput) {

		log.debug("GraphQL Query: interactionsByDateRange from {} to {}", startDate, endDate);

		Page<CampaignInteraction> interactionPage = interactionService.getInteractionsByDateRange(
				LocalDateTime.parse(startDate),
				LocalDateTime.parse(endDate),
				pageInput.toPageable()
		);

		return interactionMapper.toPageResponse(interactionPage);
	}

	@QueryMapping
	public PageResponse<MarketingInteractionOutput> marketingInteractionsByUtm(
			@Argument String utmSource,
			@Argument String utmMedium,
			@Argument String utmCampaign,
			@Argument PageInput pageInput) {

		log.debug("GraphQL Query: interactions By Utm with source: {}, medium: {}, campaign: {}",
				utmSource, utmMedium, utmCampaign);

		Page<CampaignInteraction> interactionPage = interactionService.getInteractionsByUtm(
				utmSource,
				utmMedium,
				utmCampaign,
				pageInput.toPageable()
		);

		return interactionMapper.toPageResponse(interactionPage);
	}

	@QueryMapping
	public InteractionStatisticsOutput marketingInteractionStatistics(@Argument @NotNull @Positive Long campaignId) {
		log.debug("GraphQL Query: interactionStatistics for campaign: {}", campaignId);

		InteractionStatistics statistics = interactionService.getInteractionStatistics(new MarketingCampaignId(campaignId));

		return interactionMapper.toStatisticsResponse(statistics);
	}

	@QueryMapping
	public Double conversionRate(@Argument @NotNull @Positive Long campaignId) {
		log.debug("GraphQL Query: conversionRate for campaign: {}", campaignId);

		return interactionService.getConversionRate(new MarketingCampaignId(campaignId));
	}

	@QueryMapping
	public Double averageConversionValue(@Argument @NotNull @Positive Long campaignId) {
		log.debug("GraphQL Query: averageConversionValue for campaign: {}", campaignId);

		var campaignIdVO = new MarketingCampaignId(campaignId);
		BigDecimal avgValue = interactionService.getAverageConversionValue(campaignIdVO);

		return avgValue != null ? avgValue.doubleValue() : 0.0;
	}

	@MutationMapping
	public MarketingInteractionOutput trackMarketingInteraction(@Argument @Valid TrackInteractionInput input) {
		log.debug("GraphQL Mutation: trackInteraction for campaign: {}, customer: {}",
				input.campaignId(), input.customerId());

		TrackInteractionCommand command = new TrackInteractionCommand(
				new MarketingCampaignId(input.campaignId()),
				new CustomerCompanyId(input.customerId()),
				input.interactionType(),
				input.sessionId(),
				input.channelId() != null ? new MarketingChannelId(input.channelId()) : null,
				input.utmSource(),
				input.utmMedium(),
				input.utmCampaign(),
				input.utmContent(),
				input.utmTerm(),
				input.deviceType(),
				input.deviceOs(),
				input.browser(),
				input.countryCode(),
				input.city(),
				input.landingPageUrl(),
				input.referrerUrl(),
				parseJsonToMap(input.properties())
		);

		CampaignInteraction interaction = interactionService.trackInteraction(command);

		return interactionMapper.toResponse(interaction);
	}

	@MutationMapping
	public MarketingInteractionOutput markAsConversion(@Argument @Valid MarkConversionInput input) {
		log.debug("GraphQL Mutation: markAsConversion for interaction: {}", input.interactionId());

		MarkAsConversionCommand command = new MarkAsConversionCommand(
				new CampaignInteractionId(input.interactionId()),
				new DealId(input.dealId()),
				BigDecimal.valueOf(input.conversionValue())
		);

		CampaignInteraction interaction = interactionService.markAsConversion(command);

		return interactionMapper.toResponse(interaction);
	}

	@MutationMapping
	public boolean deleteMarketingInteraction(@Argument @NotNull @Positive Long id) {
		log.debug("GraphQL Mutation: deleteInteraction with id: {}", id);

		interactionService.deleteInteraction(new CampaignInteractionId(id));

		return true;
	}


	private InteractionQuery buildInteractionQuery(InteractionFilterInput filter) {
		if (filter == null) {
			return InteractionQuery.empty();
		}

		return new InteractionQuery(
				filter.campaignId(),
				filter.customerId(),
				filter.channelId(),
				filter.interactionTypes(),
				filter.isConversion(),
				filter.dateFrom() != null ? LocalDateTime.parse(filter.dateFrom()) : null,
				filter.dateTo() != null ? LocalDateTime.parse(filter.dateTo()) : null,
				filter.utmSource(),
				filter.utmMedium(),
				filter.utmCampaign(),
				filter.deviceType(),
				filter.countryCode()
		);
	}

	private Map<String, Object> parseJsonToMap(String json) {
		if (json == null || json.isBlank()) {
			return null;
		}

		try {
			return objectMapper.readValue(json, Map.class);
		} catch (Exception e) {
			log.warn("Failed to parse JSON: {}", json, e);
			return null;
		}
	}
}
